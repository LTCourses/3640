package unit.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import paymentgateway.logic.CardIssuer;
import paymentgateway.logic.CardValidator;
import paymentgateway.logic.CreditCard;
import paymentgateway.logic.CreditCardDatabase;
import paymentgateway.logic.FundsStatus;
import paymentgateway.logic.OtherBank;
import paymentgateway.logic.PaymentLogic;
import paymentgateway.logic.PaymentStatus;

public class PaymentLogicTest {
	private CardValidator cardValidator;
	private OtherBank otherBank;
	private CreditCardDatabase creditCardDatabase;
	private PaymentLogic sut;
	
	@Before
	public void setup() {
		cardValidator = mock(CardValidator.class);
		otherBank = mock(OtherBank.class);
		creditCardDatabase = mock(CreditCardDatabase.class);
		when(creditCardDatabase.open(null, null)).thenReturn(creditCardDatabase);
		sut = new PaymentLogic(cardValidator, otherBank, creditCardDatabase);
		sut.setCardIssuer(CardIssuer.VISA);
	}
	
	@Test
	public void noAccountKey() {
		when(cardValidator.validate(anyString(), anyString(), anyString())).thenReturn(true);
		when(cardValidator.getCardType()).thenReturn(CardIssuer.VISA);
		
		sut.process(false, null, null, null, "");
		
		assertEquals(PaymentStatus.APPROVED, sut.getPaymentStatus());
		assertEquals(PaymentStatus.APPROVED.message, sut.getMessage());
		assertEquals(CardIssuer.VISA, sut.getCardType());
		assertEquals(null, sut.getFundsStatus());
		assertNotNull(sut.getTestModeMessage());
	}
	
	@Test
	public void invalidAccountKey() {
		sut.process(false, null, null, null, "INVALID");
		
		assertEquals(PaymentStatus.UNAUTHORISED, sut.getPaymentStatus());
		assertEquals(PaymentStatus.UNAUTHORISED.message, sut.getMessage());
		assertEquals(null, sut.getCardType());
		assertEquals(null, sut.getFundsStatus());
		assertNull(sut.getTestModeMessage());
	}
	
	@Test
	public void invalidCard() {
		when(creditCardDatabase.getAccount(anyString())).thenReturn("");
		when(cardValidator.validate(anyString(), anyString(), anyString())).thenReturn(false);
		when(cardValidator.getPaymentStatus()).thenReturn(PaymentStatus.INVALID_CARD_OR_EXPIRY);
		when(cardValidator.getMessage()).thenReturn("m");
		when(cardValidator.getCardType()).thenReturn(CardIssuer.VISA);
		
		sut.process(false, null, null, null, null);
		
		assertEquals(PaymentStatus.INVALID_CARD_OR_EXPIRY, sut.getPaymentStatus());
		assertEquals("m", sut.getMessage());
		assertEquals(CardIssuer.VISA, sut.getCardType());
		assertEquals(null, sut.getFundsStatus());
		assertNull(sut.getTestModeMessage());
	}
	
	@Test
	public void validCard() {
		when(creditCardDatabase.getAccount(anyString())).thenReturn("");
		when(cardValidator.validate(anyString(), anyString(), anyString())).thenReturn(true);
		when(cardValidator.getCardType()).thenReturn(CardIssuer.VISA);
		when(creditCardDatabase.getCreditCard(null, null)).thenReturn(new CreditCard("", "0399", 1000, false));
		
		sut.process(false, null, null, "1000", "");
		
		assertEquals(PaymentStatus.APPROVED, sut.getPaymentStatus());
		assertEquals(PaymentStatus.APPROVED.message, sut.getMessage());
		assertEquals(CardIssuer.VISA, sut.getCardType());
		assertEquals(FundsStatus.PAID, sut.getFundsStatus());
		assertNull(sut.getTestModeMessage());
	}
	
	@Test
	public void validateOnlyTransaction() {
		when(creditCardDatabase.getAccount(anyString())).thenReturn("");
		when(cardValidator.validate(anyString(), anyString(), anyString())).thenReturn(true);
		when(cardValidator.getCardType()).thenReturn(CardIssuer.VISA);
		when(creditCardDatabase.getCreditCard(null, null)).thenReturn(new CreditCard("", "0399", 1000, false));
		
		sut.process(true, null, null, "1000", "");
		
		assertEquals(PaymentStatus.APPROVED, sut.getPaymentStatus());
		assertEquals(PaymentStatus.APPROVED.message, sut.getMessage());
		assertEquals(CardIssuer.VISA, sut.getCardType());
		assertEquals(FundsStatus.AVAILABLE, sut.getFundsStatus());
		assertNull(sut.getTestModeMessage());
	}
	
	@Test
	public void overLimitCard() {
		when(creditCardDatabase.getAccount(anyString())).thenReturn("");
		when(cardValidator.validate(anyString(), anyString(), anyString())).thenReturn(true);
		when(cardValidator.getCardType()).thenReturn(CardIssuer.VISA);
		when(creditCardDatabase.getCreditCard(null, null)).thenReturn(new CreditCard("", "0399", 1000, false));
		
		sut.process(false, null, null, "1000.01", "");
		
		assertEquals(PaymentStatus.OVER_LIMIT, sut.getPaymentStatus());
		assertEquals(PaymentStatus.OVER_LIMIT.message, sut.getMessage());
		assertEquals(CardIssuer.VISA, sut.getCardType());
		assertEquals(null, sut.getFundsStatus());
		assertNull(sut.getTestModeMessage());
	}
	
	@Test
	public void noMatchingCard() {
		when(creditCardDatabase.getAccount(anyString())).thenReturn("");
		when(cardValidator.validate(anyString(), anyString(), anyString())).thenReturn(true);
		when(cardValidator.getCardType()).thenReturn(CardIssuer.VISA);
		
		sut.process(false, null, null, "123.45", "");
		
		assertEquals(PaymentStatus.INVALID_CARD_OR_EXPIRY, sut.getPaymentStatus());
		assertEquals(PaymentStatus.INVALID_CARD_OR_EXPIRY.message, sut.getMessage());
		assertEquals(CardIssuer.VISA, sut.getCardType());
		assertEquals(null, sut.getFundsStatus());
		assertNull(sut.getTestModeMessage());
	}
	
	@Test
	public void blockedCard() {
		when(creditCardDatabase.getAccount(anyString())).thenReturn("");
		when(cardValidator.validate(anyString(), anyString(), anyString())).thenReturn(true);
		when(cardValidator.getCardType()).thenReturn(CardIssuer.VISA);
		when(creditCardDatabase.getCreditCard(null, null)).thenReturn(new CreditCard("", "0399", 1000, true));
		
		sut.process(false, null, null, "123.45", "");
		
		assertEquals(PaymentStatus.CARD_BLOCKED, sut.getPaymentStatus());
		assertEquals(PaymentStatus.CARD_BLOCKED.message, sut.getMessage());
		assertEquals(CardIssuer.VISA, sut.getCardType());
		assertEquals(null, sut.getFundsStatus());
		assertNull(sut.getTestModeMessage());
	}
	
	@Test
	public void otherBanksCard() {
		when(creditCardDatabase.getAccount(anyString())).thenReturn("");
		when(cardValidator.validate(anyString(), anyString(), anyString())).thenReturn(true);
		when(cardValidator.getCardType()).thenReturn(CardIssuer.MASTERCARD);
		when(otherBank.getPaymentStatus()).thenReturn(PaymentStatus.APPROVED);
		when(otherBank.getMessage()).thenReturn("m");
		
		sut.process(false, null, null, null, "");
		
		assertEquals(PaymentStatus.APPROVED, sut.getPaymentStatus());
		assertEquals("m", sut.getMessage());
		assertEquals(CardIssuer.MASTERCARD, sut.getCardType());
		assertEquals(null, sut.getFundsStatus());
		assertNull(sut.getTestModeMessage());
	}
}

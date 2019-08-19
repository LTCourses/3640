package unit.logic;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import paymentgateway.logic.CardIssuer;
import paymentgateway.logic.CardValidator;
import paymentgateway.logic.PaymentStatus;

@RunWith(value = Parameterized.class)
public class CardValidatorTest {
	private String number, expiry, amount;
	private CardValidator sut;
	private boolean expectedResult;
	private PaymentStatus expectedStatus;
	private CardIssuer expectedCardType;
	
	@Parameterized.Parameters(name = "{index}: validate({0}, {1}, {2}) => {3} ({4}, {5})")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{"", "0399", "123.45", false, "NO_NUMBER", null},
			{"4111111111111111", "", "123.45", false, "NO_EXPIRY", null},
			{"4111111111111111", "123", "123.45", false, "INVALID_EXPIRY_FORMAT", null},
			{"4111111111111111", "12345", "123.45", false, "INVALID_EXPIRY_FORMAT", null},
			{"4111111111111111", "1334", "123.45", false, "INVALID_EXPIRY_FORMAT", null},
			{"4111111111111111", "0034", "123.45", false, "INVALID_EXPIRY_FORMAT", null},
			{"4111111111111111", "0399", "", false, "NO_AMOUNT", null},
			{"4111111111111111", "0399", "A", false, "INVALID_AMOUNT_FORMAT", null},
			{"4111111111111111", "0399", "1.234", false, "INVALID_AMOUNT_FORMAT", null},
			{"4111111111111112", "0399", "123.45", false, "INVALID_NUMBER", null},
			
			{"341111111111111", "0399", "123.45", true, null, "AMEX"},
			{"378282246310005", "0399", "123.45", true, null, "AMEX"},
			{"30000000000004", "0399", "123.45", true, null, "DINERS"},
			{"36300540133875", "0399", "123.45", true, null, "DINERS"},
			{"6011111111111117", "0399", "123.45", true, null, "DISCOVER"},
			// TODO Discover starting with 622126
			// TODO Discover starting with 644
			{"6500000000000002", "0399", "123.45", true, null, "DISCOVER"},
			{"6373936375413581", "0399", "123.45", true, null, "INSTAPAYMENT"},
			{"3535353535353537", "0399", "123.45", true, null, "JCB"},
			{"6706253647758969", "0399", "123.45", true, null, "LASER"},
			{"6709000000000001", "0399", "123.45", true, null, "LASER"},
			{"6771952727724268", "0399", "123.45", true, null, "LASER"},
			{"5018000000000009", "0399", "123.45", true, null, "MAESTRO"},
			{"5020000000000005", "0399", "123.45", true, null, "MAESTRO"},
			{"5038000000000005", "0399", "123.45", true, null, "MAESTRO"},
			{"5893000000000009", "0399", "123.45", true, null, "MAESTRO"},
			// TODO Maestro starting with 6759
			// TODO Maestro starting with 6761
			{"5555555555555557", "0399", "123.45", true, "APPROVED", "MASTERCARD"},
			// TODO Mastercard starting with 222100
			{"4111111111111111", "0399", "123.45", true, null, "VISA"},
		});
	}
	
	public CardValidatorTest(String number, String expiry, String amount, boolean expectedResult, String expectedStatus, String expectedCardType) {
		this.number = number;
		this.expiry = expiry;
		this.amount = amount;
		this.expectedResult = expectedResult;
		this.expectedStatus = expectedStatus == null ? null : PaymentStatus.valueOf(expectedStatus);
		this.expectedCardType = expectedCardType == null ? null : CardIssuer.valueOf(expectedCardType);
	}
	
	@Before
	public void setup()
	{
		sut = new CardValidator();
	}

	@Test
	public void validate() {
		boolean result = sut.validate(number, expiry, amount);
		assertEquals(expectedResult, result);
		if (!expectedResult) {
			assertEquals(expectedStatus, sut.getPaymentStatus());
			assertEquals(expectedCardType, sut.getCardType());
		}
	}
}

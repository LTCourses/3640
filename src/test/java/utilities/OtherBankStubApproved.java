package utilities;

import paymentgateway.logic.OtherBank;
import paymentgateway.logic.PaymentStatus;

public class OtherBankStubApproved extends OtherBank {
	@Override
	public void sendPayment(boolean isValidateOnly, String number, String expiry, String amount) {
		super.setResponse(PaymentStatus.APPROVED);
	}
}

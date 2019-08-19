Feature: Parameters
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
	Check format tests for the parameters

Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And the account key is "AABBCCD"
	
Scenario: No number
	Given the expiry is "0399"
	And the amount is $123.45
	When I process the payment
	Then the payment status should be "NO_NUMBER"
	And the card type should be blank
	And the funds should be blank

Scenario: Invalid number format
	Given the card number is "ABCDEFGHIJKLMNOP"
	And the expiry is "0399"
	And the amount is $123.45
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be blank
	And the funds should be blank
	
Scenario: Invalid number
	Given the card number is "4111111111111110"
	And the expiry is "0399"
	And the amount is $123.45
	When I process the payment
	Then the payment status should be "INVALID_NUMBER"
	And the card type should be blank
	And the funds should be blank

Scenario: No expiry
	Given the card number is "4111111111111111"
	And the amount is $123.45
	When I process the payment
	Then the payment status should be "NO_EXPIRY"
	And the card type should be blank
	And the funds should be blank

Scenario: Invalid expiry format
	Given the card number is "4111111111111111"
	And the expiry is "ABCD"
	And the amount is $123.45
	When I process the payment
	Then the payment status should be "INVALID_EXPIRY_FORMAT"
	And the card type should be blank
	And the funds should be blank

Scenario: Invalid month
	Given the card number is "4111111111111111"
	And the expiry is "1300"
	And the amount is $123.45
	When I process the payment
	Then the payment status should be "INVALID_EXPIRY_FORMAT"
	And the card type should be blank
	And the funds should be blank

Scenario: No amount
	Given the card number is "4111111111111111"
	And the expiry is "0399"
	When I process the payment
	Then the payment status should be "NO_AMOUNT"
	And the card type should be blank
	And the funds should be blank

Scenario: Invalid amount format
	Given the card number is "4111111111111111"
	And the expiry is "0399"
	And the amount is $ABC.DE
	When I process the payment
	Then the payment status should be "INVALID_AMOUNT_FORMAT"
	And the card type should be blank
	And the funds should be blank

Scenario: Too many decimal places
	Given the card number is "4111111111111111"
	And the expiry is "0399"
	And the amount is $123.456
	When I process the payment
	Then the payment status should be "INVALID_AMOUNT_FORMAT"
	And the card type should be blank
	And the funds should be blank

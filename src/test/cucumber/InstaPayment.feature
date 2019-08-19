Feature: InstaPayment
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
	Numbers are 16 digits starting with 637-639

Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And an existing credit card "INSTAPAYMENT"/"6373936375413581"/"0399"/$1000/false
	And the account key is "AABBCCD"
	And the expiry is "0399"
	And the amount is $123.45
	
Scenario: Valid InstaPayment
	Given the card number is "6373936375413581"
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "INSTAPAYMENT"

Scenario: InstaPayment number too short
	Given the card number is "637393637541350"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "INSTAPAYMENT"

Scenario: InstaPayment number too long
	Given the card number is "63739363754135810"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "INSTAPAYMENT"

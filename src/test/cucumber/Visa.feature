Feature: Visa
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
	Numbers are 13-16 digits starting with 4.

Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And an existing credit card "VISA"/"4111111111111111"/"0399"/$1000/false
	And the account key is "AABBCCD"
	And the expiry is "0399"
	And the amount is $123.45
	
Scenario: Valid Visa
	Given the card number is "4111111111111111"
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "VISA"
	And the funds should be "PAID"

Scenario: Visa number too short
	Given the card number is "411111111117"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "VISA"
	And the funds should be blank

Scenario: Visa number too long
	Given the card number is "41111111111111113"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "VISA"
	And the funds should be blank

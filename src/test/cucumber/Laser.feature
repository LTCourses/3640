Feature: Laser
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
	Numbers are 16-19 digits starting with 6706, 6709, or 6771

Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And an existing credit card "LASER"/"6709000000000001"/"0399"/$1000/false
	And the account key is "AABBCCD"
	And the expiry is "0399"
	And the amount is $123.45
	
Scenario: Valid Laser
	Given the card number is "6709000000000001"
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "LASER"

Scenario: Laser number too short
	Given the card number is "670900000000000"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "LASER"

Scenario: Laser number too long
	Given the card number is "67090000000000000001"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "LASER"

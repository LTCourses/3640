Feature: MasterCard
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
	Numbers are 16-19 digits starting with 51-55 or 222100-272099

Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And an existing credit card "MASTERCARD"/"5555555555555557"/"0399"/$1000/false
	And the account key is "AABBCCD"
	And the expiry is "0399"
	And the amount is $123.45
	
Scenario: Valid MasterCard
	Given the card number is "5555555555555557"
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "MASTERCARD"

Scenario: MasterCard number too short
	Given the card number is "555555555555558"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "MASTERCARD"

Scenario: MasterCard number too long
	Given the card number is "55555555555555555555"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "MASTERCARD"

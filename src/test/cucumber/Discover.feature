Feature: Discover
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
	Numbers are 16 digits starting with 6011, 622126, 644, or 65

Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And an existing credit card "DISCOVER"/"6011111111111117"/"0399"/$1000/false
	And the account key is "AABBCCD"
	And the expiry is "0399"
	And the amount is $123.45
	
Scenario: Valid Discover
	Given the card number is "6011111111111117"
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "DISCOVER"

Scenario: Discover number too short
	Given the card number is "601111111111116"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "DISCOVER"

Scenario: Discover number too long
	Given the card number is "60111111111111113"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "DISCOVER"

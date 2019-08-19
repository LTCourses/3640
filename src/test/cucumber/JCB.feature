Feature: JCB
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
	Numbers are 16 digits starting with 3528-3589

Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And an existing credit card "JCB"/"3535353535353537"/"0399"/$1000/false
	And the account key is "AABBCCD"
	And the expiry is "0399"
	And the amount is $123.45
	
Scenario: Valid JCB
	Given the card number is "3535353535353537"
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "JCB"

Scenario: JCB number too short
	Given the card number is "353535353535352"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "JCB"

Scenario: JCB number too long
	Given the card number is "35353535353535358"
	When I process the payment
	Then the payment status should be "INVALID_NUMBER_FORMAT"
	And the card type should be "JCB"

Feature: Validation
	As a vendor
	I want to validate the credit card payment
	So that I don't supply goods with no payment
	
	Check validation of a payment

Background:
	Given an account 42/"TestUI"/"AABBCCD"
	And an existing credit card "VISA"/"4111111111111111"/"0399"/$1000/false
	And an existing credit card "VISA"/"4222222222222220"/"0599"/$2000/false
	And an existing credit card "VISA"/"4222222222222"/"0199"/$1000/false
	And an existing credit card "VISA"/"4444444444444448"/"0399"/$1000/true
	And the account key is "AABBCCD"
	
Scenario: Wrong card number
	Given the card number is "4222222222222"
	And the expiry is "0399"
	And the amount is $123.45
	When I process the payment
	Then the payment status should be "INVALID_CARD_OR_EXPIRY"
	And the card type should be "VISA"
	And the funds should be blank

Scenario: Wrong expiry
	Given the card number is "4111111111111111"
	And the expiry is "1278"
	And the amount is $123.45
	When I process the payment
	Then the payment status should be "INVALID_CARD_OR_EXPIRY"
	And the card type should be "VISA"
	And the funds should be blank

Scenario: Expired card
	Given the card number is "4222222222222"
	And the expiry is "0199"
	And the amount is $123.45
	When I process the payment
	Then the payment status should be "EXPIRED_CARD"
	And the card type should be "VISA"
	And the funds should be blank

Scenario: Locked card
	Given the card number is "4444444444444448"
	And the expiry is "0399"
	And the amount is $123.45
	When I process the payment
	Then the payment status should be "CARD_BLOCKED"
	And the card type should be "VISA"
	And the funds should be blank

Scenario: Negative amount
	Given the card number is "4111111111111111"
	And the expiry is "0399"
	And the amount is $-0.01
	When I process the payment
	Then the payment status should be "INVALID_AMOUNT_FORMAT"
	And the card type should be blank
	And the funds should be blank

Scenario: At the limit - 1
	Given the card number is "4111111111111111"
	And the expiry is "0399"
	And the amount is $1000.00
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "VISA"
	And the funds should be "PAID"

Scenario: Over the limit - 1
	Given the card number is "4111111111111111"
	And the expiry is "0399"
	And the amount is $1000.01
	When I process the payment
	Then the payment status should be "OVER_LIMIT"
	And the card type should be "VISA"
	And the funds should be blank

Scenario: At the limit - 2
	Given the card number is "4222222222222220"
	And the expiry is "0599"
	And the amount is $2000.00
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "VISA"
	And the funds should be "PAID"

Scenario: Over the limit - 2
	Given the card number is "4222222222222220"
	And the expiry is "0599"
	And the amount is $2000.01
	When I process the payment
	Then the payment status should be "OVER_LIMIT"
	And the card type should be "VISA"
	And the funds should be blank

Scenario: Validation only
	Given the card number is "4111111111111111"
	And the expiry is "0399"
	And the amount is $123.45
	And the transaction is validate only
	When I process the payment
	Then the payment status should be "APPROVED"
	And the card type should be "VISA"
	And the funds should be "AVAILABLE"

[when]a customer has average earnings less than {earnings} = $customer : Customer( averageEarnings < {earnings} )
[then]offer a loan to the customer = logger.info("Sending loan offer");insert(new MarketingMessage($customer, Content.LOAN));
[when]there is an account with balance more than {balance} = $account : Account( balance > {balance})
[then]offer a gold credit card to the owner = logger.info("Sending gold card offer");insert(new MarketingMessage($account.getCustomer(), Content.GOLD_CARD));
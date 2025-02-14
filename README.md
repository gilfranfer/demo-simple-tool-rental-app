# Simple Tool Rental App (Demo)

The application is a point-of-sale tool for a store that rents big tools.
- Customers rent a tool for a specified number of days.
- When a customer checks out a tool, a Rental Agreement is produced.
- The store charges a daily rental fee, whose amount is different for each tool type.
- Some tools are free of charge on weekends or holidays.
- Clerks may give customers a discount that is applied to the total daily charges to reduce the final charge.

*Assumption*: We are charging for the checkout date, and not charging for the due date. 

For example, If the tool was checkout out on Friday, June 28th, for 1 day, then:
- Checkout date is 06/28/2024
- Due date is 06/29/2024
- We are charging only for 1 day, that is the Friday, June 28th (assuming the tool has a charge on weekday and 06/28 is not a holiday).

## Tools
The tools available for rental are as follows:

| Tool Code | Tool Type  | Brand  |
|-----------|------------|--------|
| CHNS      | Chainsaw   | Stihl  |
| LADW      | Ladder     | Werner |
| JAKD      | Jackhammer | DeWalt |
| JAKR      | Jackhammer | Ridgid |

Each tool instance has the following attributes:

- Tool Code: Unique identifier for a tool instance
- Tool Type: The type of tool. The type also specifies the daily rental charge, and the days for which the
daily rental charge applies.
- Brand: The brand of the ladder, chain saw or jackhammer.

| Tool       | Daily charge | Weekday charge | Weekend charge | Holiday charge |
|------------|--------------|----------------|----------------|----------------|
| Ladder     | $1.99        | Yes            | Yes            | No             |
| Chainsaw   | $1.49        | Yes            | No             | Yes            |
| Jackhammer | $2.99        | Yes            | No             | No             |

## Holidays

There are only two (2) holidays in the calendar:
- Independence Day, July 4th: If falls on weekend, it is observed on the closest weekday (if Sat,
then Friday before, if Sunday, then Monday after)
- Labor Day: First Monday in September

## Checkout

### Checkout requires the following information to be provided:
- Tool code: See tool table above
- Rental day count: The number of days for which the customer wants to rent the tool. (e.g. 4
days)
- Discount percent: As a whole number, 0-100 (e.g. 20 = 20%)
- Check out date

### Checkout should throw an exception with an instructive, user-friendly message if
- Rental day count is not 1 or greater
- Discount percent is not in the range 0-100

### Checkout generates a Rental Agreement instance with the following values.
- Tool code: Specified at checkout
- Tool type: From tool info
- Tool brand: From tool info
- Rental days: Specified at checkout
- Check out date: Specified at checkout
- Due date: Calculated from checkout date and rental days.
- Daily rental charge: Amount per day, specified by the tool type.
- Charge days: Count of chargeable days, from day after checkout through and including due
date, excluding “no charge” days as specified by the tool type.
- Pre-discount charge: Calculated as charge days X daily charge. Resulting total rounded half up
to cents.
- Discount percent: Specified at checkout.
- Discount amount: calculated from discount % and pre-discount charge. Resulting amount
rounded half up to cents.
- Final charge: Calculated as pre-discount charge - discount amount.

# Tests

The test scenarios are located in: _src\test\java\com\gillab\service\CheckoutServiceTest_, in the class CheckoutTests.
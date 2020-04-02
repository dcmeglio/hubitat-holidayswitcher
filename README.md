# hubitat-holidayswitcher
Provides an easy way to toggle a switch whenever it's a holiday. This is especially useful for floating holidays such as Easter.

## Calendarific API
To use this app you will need an API key for [Calendarific](https://calendarific.com/). You can obtain one free by clicking on the [link](https://calendarific.com/). Simply register on the website, then when you receive an email verification, verify it then login. On the website you will see a line that says __API KEY (use as password in external tools)__. Copy this value. The free account gives you 1000 API calls. Other than when you configure the app, it will only make an API call once per year so the free account is more than enough.

## Configuration
When you configure the app, enter the API Key from the Calendarific website and choose your country. This will then list the national holidays, religious holidays, and observances that are valid for your country. Choose the holidays you'd like. The dates of the holidays will automatically update if they differ year by year.

You can also define custom holidays. If there are days you wish to include that aren't in the list, toggle the __Define custom holidays?__ switch and enter how many holidays you want to define. You can then enter in the dates.

Finally, choose the switches that should be turned on when it's a holiday and turned off when it is not. The switches will toggle at 12:00:01 AM.

## Donations
If you find this app useful, please consider making a [donation](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=7LBRPJRLJSDDN&source=url)! 
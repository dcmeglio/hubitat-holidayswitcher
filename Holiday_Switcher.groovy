/**
 *  Holiday Switcher
 *
 *  Copyright 2019 Dominick Meglio
 *
 */

import groovy.transform.Field

definition(
    name: "Holiday Switcher",
    namespace: "dcm.holiday",
    author: "Dominick Meglio",
    description: "Determines whether or not it is a holiday and sets a virtual switch",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	page(name: "prefAccount", title: "Calendarific API Access", nextPage: "prefDetails")
	page(name: "prefDetails", title: "Holiday Details" )
}

def prefAccount() {
	return dynamicPage(name: "prefAccount", title: "Connect to Calendarific API", nextPage:"prefDetails", uninstall:false, install: false) {
		section("API Access"){
			paragraph "You can obtain a free API key going to <a href='https://calendarific.com'>https://calendarific.com</a>"
			input("calAPIToken", "text", title: "API Key", description: "API Key", required: true)
			input("calCountry", "enum", title: "Country", description: "Country", options: countryList, required: true)
		}
	}
}

def prefDetails() {
	getHolidays()
    dynamicPage(name: "prefDetails", title: "Holidays and Settings", install: true, uninstall: false) {
        section("Holidays") {
			input(name:"calNational", type: "enum", title: "National Holidays", options:state.nationalHolidays, required:false, multiple:true)
			input(name:"calReligious", type: "enum", title: "Religious Holidays", options:state.religiousHolidays, required:false, multiple:true)
			input(name:"calObservances", type: "enum", title: "Observances", options:state.observanceHolidays, required:false, multiple:true)
			section("Custom Holidays") {
				input("customHolidays", "bool", title: "Define custom holidays?",defaultValue: false, displayDuringSetup: true, submitOnChange: true)
				if (customHolidays)
				{
					input(name: "customHolidayCount", type: "number", range: "1..366", title: "How many custom holidays?", required: true, submitOnChange: true)
					if (customHolidayCount > 0)
					{
						for (def i = 0; i < customHolidayCount; i++)
							input(name:"customHoliday${i}", type:"date", title: "Custom Holiday ${i+1}", required: true)
					}
				}
			}
			

        }
		section ("Switches") {
			input("holidaySwitches", "capability.switch", required: true, multiple: true)
		}
		section ("Settings") {
			input("debugOutput", "bool", title: "Enable debug logging?", defaultValue: true, displayDuringSetup: false, required: false)
		}
    }
}

@Field countryList = [
'us': 'United States',
'af': 'Afghanistan',
'al': 'Albania',
'dz': 'Algeria',
'as': 'American Samoa',
'ad': 'Andorra',
'ao': 'Angola',
'ai': 'Anguilla',
'ag': 'Antigua and Barbuda',
'ar': 'Argentina',
'am': 'Armenia',
'aw': 'Aruba',
'au': 'Australia',
'at': 'Austria',
'az': 'Azerbaijan',
'bh': 'Bahrain',
'bd': 'Bangladesh',
'bb': 'Barbados',
'by': 'Belarus',
'be': 'Belgium',
'bz': 'Belize',
'bj': 'Benin',
'bm': 'Bermuda',
'bt': 'Bhutan',
'bo': 'Bolivia',
'ba': 'Bosnia and Herzegovina',
'bw': 'Botswana',
'br': 'Brazil',
'vg': 'British Virgin Islands',
'bn': 'Brunei',
'bg': 'Bulgaria',
'bf': 'Burkina Faso',
'bi': 'Burundi',
'cv': 'Cabo Verde',
'kh': 'Cambodia',
'cm': 'Cameroon',
'ca': 'Canada',
'ky': 'Cayman Islands',
'cf': 'Central African Republic',
'td': 'Chad',
'cl': 'Chile',
'cn': 'China',
'co': 'Colombia',
'km': 'Comoros',
'cg': 'Congo',
'cd': 'Congo Democratic Republic',
'ck': 'Cook Islands',
'cr': 'Costa Rica',
'ci': 'Cote d\'Ivoire',
'hr': 'Croatia',
'cu': 'Cuba',
'cw': 'Curaçao',
'cy': 'Cyprus',
'cz': 'Czech Republic',
'dk': 'Denmark',
'dj': 'Djibouti',
'dm': 'Dominica',
'do': 'Dominican Republic',
'tl': 'East Timor',
'ec': 'Ecuador',
'eg': 'Egypt',
'sv': 'El Salvador',
'gq': 'Equatorial Guinea',
'er': 'Eritrea',
'ee': 'Estonia',
'et': 'Ethiopia',
'fk': 'Falkland Islands',
'fo': 'Faroe Islands',
'fj': 'Fiji',
'fi': 'Finland',
'fr': 'France',
'pf': 'French Polynesia',
'ga': 'Gabon',
'gm': 'Gambia',
'ge': 'Georgia',
'de': 'Germany',
'gh': 'Ghana',
'gi': 'Gibraltar',
'gr': 'Greece',
'gl': 'Greenland',
'gd': 'Grenada',
'gu': 'Guam',
'gt': 'Guatemala',
'gg': 'Guernsey',
'gn': 'Guinea',
'gw': 'Guinea-Bissau',
'gy': 'Guyana',
'ht': 'Haiti',
'va': 'Holy See (Vatican City)',
'hn': 'Honduras',
'hk': 'Hong Kong',
'hu': 'Hungary',
'is': 'Iceland',
'in': 'India',
'id': 'Indonesia',
'ir': 'Iran',
'iq': 'Iraq',
'ie': 'Ireland',
'im': 'Isle of Man',
'il': 'Israel',
'it': 'Italy',
'jm': 'Jamaica',
'jp': 'Japan',
'je': 'Jersey',
'jo': 'Jordan',
'kz': 'Kazakhstan',
'ke': 'Kenya',
'ki': 'Kiribati',
'xk': 'Kosovo',
'kw': 'Kuwait',
'kg': 'Kyrgyzstan',
'la': 'Laos',
'lv': 'Latvia',
'lb': 'Lebanon',
'ls': 'Lesotho',
'lr': 'Liberia',
'ly': 'Libya',
'li': 'Liechtenstein',
'lt': 'Lithuania',
'lu': 'Luxembourg',
'mo': 'Macau',
'mg': 'Madagascar',
'mw': 'Malawi',
'my': 'Malaysia',
'mv': 'Maldives',
'ml': 'Mali',
'mt': 'Malta',
'mh': 'Marshall Islands',
'mq': 'Martinique',
'mr': 'Mauritania',
'mu': 'Mauritius',
'yt': 'Mayotte',
'mx': 'Mexico',
'fm': 'Micronesia',
'md': 'Moldova',
'mc': 'Monaco',
'mn': 'Mongolia',
'me': 'Montenegro',
'ms': 'Montserrat',
'ma': 'Morocco',
'mz': 'Mozambique',
'mm': 'Myanmar',
'na': 'Namibia',
'nr': 'Nauru',
'np': 'Nepal',
'nl': 'Netherlands',
'nc': 'New Caledonia',
'nz': 'New Zealand',
'ni': 'Nicaragua',
'ne': 'Niger',
'ng': 'Nigeria',
'kp': 'North Korea',
'mk': 'North Macedonia',
'mp': 'Northern Mariana Islands',
'no': 'Norway',
'om': 'Oman',
'pk': 'Pakistan',
'pw': 'Palau',
'pa': 'Panama',
'pg': 'Papua New Guinea',
'py': 'Paraguay',
'pe': 'Peru',
'ph': 'Philippines',
'pl': 'Poland',
'pt': 'Portugal',
'pr': 'Puerto Rico',
'qa': 'Qatar',
're': 'Reunion',
'ro': 'Romania',
'ru': 'Russia',
'rw': 'Rwanda',
'sh': 'Saint Helena',
'kn': 'Saint Kitts and Nevis',
'lc': 'Saint Lucia',
'mf': 'Saint Martin',
'pm': 'Saint Pierre and Miquelon',
'vc': 'Saint Vincent and the Grenadines',
'ws': 'Samoa',
'sm': 'San Marino',
'st': 'Sao Tome and Principe',
'sa': 'Saudi Arabia',
'sn': 'Senegal',
'rs': 'Serbia',
'sc': 'Seychelles',
'sl': 'Sierra Leone',
'sg': 'Singapore',
'sx': 'Sint Maarten',
'sk': 'Slovakia',
'si': 'Slovenia',
'sb': 'Solomon Islands',
'so': 'Somalia',
'za': 'South Africa',
'kr': 'South Korea',
'ss': 'South Sudan',
'es': 'Spain',
'lk': 'Sri Lanka',
'bl': 'St. Barts',
'sd': 'Sudan',
'sr': 'Suriname',
'se': 'Sweden',
'ch': 'Switzerland',
'sy': 'Syria',
'tw': 'Taiwan',
'tj': 'Tajikistan',
'tz': 'Tanzania',
'th': 'Thailand',
'bs': 'The Bahamas',
'tg': 'Togo',
'to': 'Tonga',
'tt': 'Trinidad and Tobago',
'tn': 'Tunisia',
'tr': 'Turkey',
'tm': 'Turkmenistan',
'tc': 'Turks and Caicos Islands',
'tv': 'Tuvalu',
'vi': 'US Virgin Islands',
'ug': 'Uganda',
'ua': 'Ukraine',
'ae': 'United Arab Emirates',
'gb': 'United Kingdom',
'uy': 'Uruguay',
'uz': 'Uzbekistan',
'vu': 'Vanuatu',
've': 'Venezuela',
'vn': 'Vietnam',
'wf': 'Wallis and Futuna',
'ye': 'Yemen',
'zm': 'Zambia',
'zw': 'Zimbabwe',
'sz': 'eSwatini'
]

def installed() {
	logDebug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	logDebug "Updated with settings: ${settings}"
    unschedule()
	unsubscribe()
	initialize()
}

def uninstalled() {
	logDebug "Uninstalled app"
}

def initialize() {
	logDebug "initializing"
	
	schedule("01 00 00 ? * *", checkHoliday)
	checkHoliday()
}

def checkHoliday()
{
	def today = timeToday(null, location.timeZone)
	def year = today.year+1900
	def month = today.month+1
	def day = today.date
	
	// Refresh the holidays for this year
	if (month == 1 && day == 1)
		getHolidays()
	
	if (isHoliday(calNational, state.nationalHolidaysList, year, month, day) ||
		isHoliday(calReligious, state.religiousHolidaysList, year, month, day) ||
		isHoliday(calObservances, state.observanceHolidaysList, year, month, day) || isCustomHoliday(year, month, day))
	{
		holidaySwitches.on()
	}
	else
		holidaySwitches.off()
		
}

def isCustomHoliday(year, month, day)
{
	if (customHolidays && customHolidayCount > 0)
	{
		for (def i = 0; i < customHolidayCount; i++)
		{
			def date = timeToday(this.getProperty("customHoliday${i}"), location.timeZone)
			if (year == date.year+1900 && month == date.month+1 && day == date.date)
				return true
		}
	}
	return false
}

def isHoliday(holidays, fulllist, year, month, day)
{
	for (holiday in holidayList)
	{
		def holidayDate = fulllist[holiday]
		if (holidayDate.year == year && holidayDate.month == month && holidayDate.day == day)
			return true
	}
	return false
}

def extractHolidays(holidayList)
{
	def result = [:]
	
	for(holiday in holidayList)
	{
		result[holiday.name] = holiday.name
	}
	
	return result
}

def extractHolidayDetails(holidayList)
{
	def result = [:]
	
	for(holiday in holidayList)
	{
		result[holiday.name] = holiday.date.datetime
	}
	
	return result
}

def getHolidays()
{
	state.nationalHolidays = [:]
	state.religiousHolidays = [:]
	state.observanceHolidays = [:]
	state.nationalHolidaysList = [:]
	state.religiousHolidaysList = [:]
	state.observanceHolidaysList = [:]
    def result = sendApiRequest("national", "GET")

	if (result.status == 200) {
		state.nationalHolidays = extractHolidays(result.data.response.holidays)
		state.nationalHolidaysList = extractHolidayDetails(result.data.response.holidays)
	}
		
	result = sendApiRequest("religious", "GET")
	if (result.status == 200) {
		state.religiousHolidays = extractHolidays(result.data.response.holidays)
		state.religiousHolidaysList = extractHolidayDetails(result.data.response.holidays)
	}
		
	result = sendApiRequest("observance", "GET")
	if (result.status == 200) {
		state.observanceHolidays = extractHolidays(result.data.response.holidays)
		state.observanceHolidaysList = extractHolidayDetails(result.data.response.holidays)
	}
}

def sendApiRequest(type, method)
{
    def params = [
		uri: "https://calendarific.com",
        path: "/api/v2/holidays",
		contentType: "application/json",
		query: [
                api_key: calAPIToken,
				country: calCountry,
				year: timeToday(null, location.timeZone).year + 1900,
				type: type
            ],
		timeout: 300
	]

    if (body != null)
        params.body = body
    
    def result = null
    if (method == "GET") {
        httpGet(params) { resp ->
            result = resp
        }
    }
    else if (method == "POST") {
        httpPost(params) { resp ->
            result = resp
        }
    }
    return result
}


def logDebug(msg) {
    if (settings?.debugOutput) {
		log.debug msg
	}
}
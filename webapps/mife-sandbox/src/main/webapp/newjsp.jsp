<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>Edit User Profile | MIFE API RI Developer Portal</title>
        <link rel="stylesheet" href="template/jquery-ui-1.css"/>
        <link rel="alternate" type="application/rss+xml" title="News (RSS)" href="http://mife.com/g/blog/feed/rss" />

        <script src="template/ga.js" async="" type="text/javascript"></script><script type="text/javascript" src="template/jquery-1.js"></script>

        <script type="text/javascript" src="template/jquery_002.js"></script>

        <script type="text/javascript" src="template/jquery.js"></script>

        <script type="text/javascript" src="template/jquery-ui-1.js"></script>


        <script type="text/javascript" src="template/jquery_003.js"></script>


        <script type="text/javascript"> 
            $(document).ready(function() { 
                    $('#translationSelect').selectbox(); 
            }); 
        </script>

        <link rel="stylesheet" href="template/960_002.css"/>
        <link rel="stylesheet" href="template/default_002.css"/>



        <link rel="stylesheet" type="text/css" href="template/960.css"/>
        <link rel="stylesheet" type="text/css" href="template/default.css"/>

        <link rel="shortcut icon" type="image/x-icon" href="http://mife.com/f/files/css/main/images/favicon.ico"/>
        <link rel="stylesheet" type="text/css" href="template/main.css"/>
        <script type="text/javascript" src="template/navmenu.js"></script>

    </head>
    <body>
        <div id="fullContainer">


            <div id="header" class="clearfix">
                <div id="header-main" class="container_12 clearfix">
                    <div class="grid_12">
                        <div id="user-controls" class="clearfix">


                            <ul>


                                <li class="loggedinUser">Hi Roshan!</li>




                                <li><a href="http://mife.com/g/user/edit"><span>Edit Profile</span></a></li>




                                <li><a href="http://mife.com/g/logout/index"><span>Log Out</span></a></li>
                            </ul>
                        </div>

                        <div id="left-logo-content">
                            <div id="devportal-logo">
                                <h1><a href="http://mife.com/">MIFE API RI Developer Portal</a></h1>
                            </div>
                        </div>
                        <div id="right-logo">
                        </div>

                    </div>
                    <div class="grid_12">



                        <div class="nav-header"><ul><li><a href="http://mife.com/">Home</a></li><li><a href="http://mife.com/g/dashboard">Dashboard</a></li><li><a href="http://mife.com/resources">Resources</a></li><li><a href="http://mife.com/g/blog">News</a></li><li><a href="http://mife.com/Info">Info</a></li><li><a href="http://mife.com/g/forum">Forum</a></li></ul></div>        

                    </div>

                </div>
            </div>

            <div class="container_12">
                <div class="grid_12">


                    <!-- Comment needed for IE7 -->
                </div>
            </div>






            <div id="body_container" class="container_12 clearfix">




                <div class="grid_12">
                    <h1>Edit User Profile</h1>
                </div>

                <div class="grid_10 prefix_1 suffix_1 clearfix break">



                </div>




                <br class="clear">
                    <br class="clear">
                        <br>

                            <div class="grid_2">
                                <p><a id="changePassword" class="button" href="http://mife.com/g/user/changePassword">Change Password</a></p>
                            </div>
                            <div class="grid_3 suffix_7">
                                <p><a id="securityResponse" class="button" href="http://mife.com/g/user/securityResponse">Change Security Details</a></p>
                            </div>


                            <div id="inputArea">
                                <form action="/g/user/update" method="post" name="userProfileForm" enctype="multipart/form-data" id="userProfileForm">
                                    <div class="grid_12 break">

                                        <div class="grid_6 alpha">
                                            <h2>Personal</h2>

                                            <p><label for="firstName">First Name</label><span class="requiredField ">*</span><br>
                                                    <input id="firstName" name="firstName" class="wide" value="Roshan" type="text" /></p>

                                            <p><label for="lastName">Last Name</label><span class="requiredField ">*</span><br>
                                                    <input id="lastName" name="lastName" class="medium" value="Saputhanthri" type="text" />

                                            </p>

                                            <p><label for="newEmail">Email</label><span class="requiredField ">*</span><br/>
                                                <input id="newEmail" name="newEmail" class="wide" value="Roshan.Saputhanthri@dialog.lk" type="text" />

                                            </p>


                                            <p>
                                                <label for="telephone">Telephone</label><br>
                                                    <input id="telephone" name="telephone" type="text"/>

                                            </p>


                                            <p>
                                                <label for="fax">Fax</label><br>
                                                    <input id="fax" name="fax" class="wide" type="text"/>

                                            </p>

                                            <p>
                                                <label for="country">Country</label><span class="requiredField ">*</span><br/>
                                                <select name="country" id="country">
                                                    <option value="">Select One...</option>
                                                    <option value="AFG">Afghanistan</option>
                                                    <option value="ALA">Aland</option>
                                                    <option value="ALB">Albania</option>
                                                    <option value="DZA">Algeria</option>
                                                    <option value="ASM">American Samoa</option>
                                                    <option value="AND">Andorra</option>
                                                    <option value="AGO">Angola</option>
                                                    <option value="AIA">Anguilla</option>
                                                    <option value="ATG">Antigua and Barbuda</option>
                                                    <option value="ARG">Argentina</option>
                                                    <option value="ARM">Armenia</option>
                                                    <option value="ABW">Aruba</option>
                                                    <option value="ASC">Ascension</option>
                                                    <option value="AUT">Austria</option>
                                                    <option value="BHS">Bahamas, The</option>
                                                    <option value="BHR">Bahrain</option>
                                                    <option value="BGD">Bangladesh</option>
                                                    <option value="BRB">Barbados</option>
                                                    <option value="BLR">Belarus</option>
                                                    <option value="BEL">Belgium</option>
                                                    <option value="BLZ">Belize</option>
                                                    <option value="BEN">Benin</option>
                                                    <option value="BMU">Bermuda</option>
                                                    <option value="BTN">Bhutan</option>
                                                    <option value="BOL">Bolivia</option>
                                                    <option value="BIH">Bosnia and Herzegovina</option>
                                                    <option value="BWA">Botswana</option>
                                                    <option value="BVT">Bouvet Island</option>
                                                    <option value="BRA">Brazil</option>
                                                    <option value="ATA">Antarctica</option>
                                                    <option value="IOT">British Indian Ocean Territory</option>
                                                    <option value="VGB">British Virgin Islands</option>
                                                    <option value="BRN">Brunei</option>
                                                    <option value="BGR">Bulgaria</option>
                                                    <option value="BFA">Burkina Faso</option>
                                                    <option value="BDI">Burundi</option>
                                                    <option value="KHM">Cambodia</option>
                                                    <option value="CMR">Cameroon</option>
                                                    <option value="CAN">Canada</option>
                                                    <option value="CPV">Cape Verde</option>
                                                    <option value="CYM">Cayman Islands</option>
                                                    <option value="CAF">Central African Republic</option>
                                                    <option value="TCD">Chad</option>
                                                    <option value="CHL">Chile</option>
                                                    <option value="CHN">China, People's Republic of</option>
                                                    <option value="TWN">China, Republic of (Taiwan)</option>
                                                    <option value="CXR">Christmas Island</option>
                                                    <option value="PYF">Clipperton Island</option>
                                                    <option value="CCK">Cocos (Keeling) Islands</option>
                                                    <option value="COL">Colombia</option>
                                                    <option value="COM">Comoros</option>
                                                    <option value="COD">Congo, Democratic Republic of the (Congo-Kinshasa)</option>
                                                    <option value="COG">Congo, Republic of the (Congo-Brazzaville)</option>
                                                    <option value="COK">Cook Islands</option>
                                                    <option value="AUS">Coral Sea Islands</option>
                                                    <option value="CRI">Costa Rica</option>
                                                    <option value="CIV">Cote d'Ivoire (Ivory Coast)</option>
                                                    <option value="HRV">Croatia</option>
                                                    <option value="CUB">Cuba</option>
                                                    <option value="CZE">Czech Republic</option>
                                                    <option value="DNK">Denmark</option>
                                                    <option value="DJI">Djibouti</option>
                                                    <option value="DMA">Dominica</option>
                                                    <option value="DOM">Dominican Republic</option>
                                                    <option value="ECU">Ecuador</option>
                                                    <option value="EGY">Egypt</option>
                                                    <option value="SLV">El Salvador</option>
                                                    <option value="GNQ">Equatorial Guinea</option>
                                                    <option value="ERI">Eritrea</option>
                                                    <option value="EST">Estonia</option>
                                                    <option value="ETH">Ethiopia</option>
                                                    <option value="FLK">Falkland Islands (Islas Malvinas)</option>
                                                    <option value="FRO">Faroe Islands</option>
                                                    <option value="FJI">Fiji</option>
                                                    <option value="FIN">Finland</option>
                                                    <option value="FRA">France</option>
                                                    <option value="GUF">French Guiana</option>
                                                    <option value="ATF">French Southern and Antarctic Lands</option>
                                                    <option value="GAB">Gabon</option>
                                                    <option value="GMB">Gambia, The</option>
                                                    <option value="DEU">Germany</option>
                                                    <option value="GHA">Ghana</option>
                                                    <option value="GIB">Gibraltar</option>
                                                    <option value="GRC">Greece</option>
                                                    <option value="GRL">Greenland</option>
                                                    <option value="GRD">Grenada</option>
                                                    <option value="GLP">Guadeloupe</option>
                                                    <option value="GUM">Guam</option>
                                                    <option value="GTM">Guatemala</option>
                                                    <option value="GGY">Guernsey</option>
                                                    <option value="GIN">Guinea</option>
                                                    <option value="GNB">Guinea-Bissau</option>
                                                    <option value="GUY">Guyana</option>
                                                    <option value="HTI">Haiti</option>
                                                    <option value="HMD">Heard Island and McDonald Islands</option>
                                                    <option value="HND">Honduras</option>
                                                    <option value="HKG">Hong Kong</option>
                                                    <option value="HUN">Hungary</option>
                                                    <option value="ISL">Iceland</option>
                                                    <option value="IND">India</option>
                                                    <option value="IDN">Indonesia</option>
                                                    <option value="IRN">Iran</option>
                                                    <option value="IRQ">Iraq</option>
                                                    <option value="IRL">Ireland</option>
                                                    <option value="IMN">Isle of Man</option>
                                                    <option value="ISR">Israel</option>
                                                    <option value="ITA">Italy</option>
                                                    <option value="JAM">Jamaica</option>
                                                    <option value="JPN">Japan</option>
                                                    <option value="JEY">Jersey</option>
                                                    <option value="JOR">Jordan</option>
                                                    <option value="KAZ">Kazakhstan</option>
                                                    <option value="KEN">Kenya</option>
                                                    <option value="KIR">Kiribati</option>
                                                    <option value="PRK">Korea, Democratic People's Republic of (North Korea)</option>
                                                    <option value="KOR">Korea, Republic of  (South Korea)</option>
                                                    <option value="SCG">Kosovo</option>
                                                    <option value="KWT">Kuwait</option>
                                                    <option value="KGZ">Kyrgyzstan</option>
                                                    <option value="LAO">Laos</option>
                                                    <option value="LVA">Latvia</option>
                                                    <option value="LBN">Lebanon</option>
                                                    <option value="LSO">Lesotho</option>
                                                    <option value="LBR">Liberia</option>
                                                    <option value="LBY">Libya</option>
                                                    <option value="LIE">Liechtenstein</option>
                                                    <option value="LTU">Lithuania</option>
                                                    <option value="LUX">Luxembourg</option>
                                                    <option value="MAC">Macau</option>
                                                    <option value="MKD">Macedonia</option>
                                                    <option value="MDG">Madagascar</option>
                                                    <option value="MWI">Malawi</option>
                                                    <option value="MYS">Malaysia</option>
                                                    <option value="MDV">Maldives</option>
                                                    <option value="MLI">Mali</option>
                                                    <option value="MLT">Malta</option>
                                                    <option value="MHL">Marshall Islands</option>
                                                    <option value="MTQ">Martinique</option>
                                                    <option value="MRT">Mauritania</option>
                                                    <option value="MUS">Mauritius</option>
                                                    <option value="MYT">Mayotte</option>
                                                    <option value="MEX">Mexico</option>
                                                    <option value="FSM">Micronesia</option>
                                                    <option value="MCO">Monaco</option>
                                                    <option value="MNG">Mongolia</option>
                                                    <option value="MNE">Montenegro</option>
                                                    <option value="MSR">Montserrat</option>
                                                    <option value="MAR">Morocco</option>
                                                    <option value="MOZ">Mozambique</option>
                                                    <option value="MMR">Myanmar (Burma)</option>
                                                    <option value="AZE">Nagorno-Karabakh</option>
                                                    <option value="NAM">Namibia</option>
                                                    <option value="NRU">Nauru</option>
                                                    <option value="NPL">Nepal</option>
                                                    <option value="NLD">Netherlands</option>
                                                    <option value="ANT">Netherlands Antilles</option>
                                                    <option value="NCL">New Caledonia</option>
                                                    <option value="NZL">New Zealand</option>
                                                    <option value="NIC">Nicaragua</option>
                                                    <option value="NER">Niger</option>
                                                    <option value="NGA">Nigeria</option>
                                                    <option value="NIU">Niue</option>
                                                    <option value="NFK">Norfolk Island</option>
                                                    <option value="CYP">Northern Cyprus</option>
                                                    <option value="MNP">Northern Mariana Islands</option>
                                                    <option value="NOR">Norway</option>
                                                    <option value="OMN">Oman</option>
                                                    <option value="PAK">Pakistan</option>
                                                    <option value="PLW">Palau</option>
                                                    <option value="PSE">Palestinian Territories (Gaza Strip and West Bank)</option>
                                                    <option value="PAN">Panama</option>
                                                    <option value="PNG">Papua New Guinea</option>
                                                    <option value="PRY">Paraguay</option>
                                                    <option value="PER">Peru</option>
                                                    <option value="PHL">Philippines</option>
                                                    <option value="PCN">Pitcairn Islands</option>
                                                    <option value="POL">Poland</option>
                                                    <option value="PRT">Portugal</option>
                                                    <option value="MDA">Pridnestrovie (Transnistria)</option>
                                                    <option value="PRI">Puerto Rico</option>
                                                    <option value="QAT">Qatar</option>
                                                    <option value="REU">Reunion</option>
                                                    <option value="ROU">Romania</option>
                                                    <option value="RUS">Russia</option>
                                                    <option value="RWA">Rwanda</option>
                                                    <option value="SHN">Saint Helena</option>
                                                    <option value="KNA">Saint Kitts and Nevis</option>
                                                    <option value="LCA">Saint Lucia</option>
                                                    <option value="SPM">Saint Pierre and Miquelon</option>
                                                    <option value="VCT">Saint Vincent and the Grenadines</option>
                                                    <option value="WSM">Samoa</option>
                                                    <option value="SMR">San Marino</option>
                                                    <option value="STP">Sao Tome and Principe</option>
                                                    <option value="SAU">Saudi Arabia</option>
                                                    <option value="SEN">Senegal</option>
                                                    <option value="SRB">Serbia</option>
                                                    <option value="SYC">Seychelles</option>
                                                    <option value="SLE">Sierra Leone</option>
                                                    <option value="SGP">Singapore</option>
                                                    <option value="SVK">Slovakia</option>
                                                    <option value="SVN">Slovenia</option>
                                                    <option value="SLB">Solomon Islands</option>
                                                    <option value="SOM">Somaliland</option>
                                                    <option value="ZAF">South Africa</option>
                                                    <option value="SGS">South Georgia and the South Sandwich Islands</option>
                                                    <option value="GEO">South Ossetia</option>
                                                    <option value="ESP">Spain</option>
                                                    <option value="LKA" selected="selected">Sri Lanka</option>
                                                    <option value="SDN">Sudan</option>
                                                    <option value="SUR">Suriname</option>
                                                    <option value="SJM">Svalbard</option>
                                                    <option value="SWZ">Swaziland</option>
                                                    <option value="SWE">Sweden</option>
                                                    <option value="CHE">Switzerland</option>
                                                    <option value="SYR">Syria</option>
                                                    <option value="TJK">Tajikistan</option>
                                                    <option value="TZA">Tanzania</option>
                                                    <option value="THA">Thailand</option>
                                                    <option value="TLS">Timor-Leste (East Timor)</option>
                                                    <option value="TGO">Togo</option>
                                                    <option value="TKL">Tokelau</option>
                                                    <option value="TON">Tonga</option>
                                                    <option value="TTO">Trinidad and Tobago</option>
                                                    <option value="TAA">Tristan da Cunha</option>
                                                    <option value="TUN">Tunisia</option>
                                                    <option value="TUR">Turkey</option>
                                                    <option value="TKM">Turkmenistan</option>
                                                    <option value="TCA">Turks and Caicos Islands</option>
                                                    <option value="TUV">Tuvalu</option>
                                                    <option value="VIR">U.S. Virgin Islands</option>
                                                    <option value="UGA">Uganda</option>
                                                    <option value="UKR">Ukraine</option>
                                                    <option value="ARE">United Arab Emirates</option>
                                                    <option value="GBR">United Kingdom</option>
                                                    <option value="USA">United States</option>
                                                    <option value="URY">Uruguay</option>
                                                    <option value="UZB">Uzbekistan</option>
                                                    <option value="VUT">Vanuatu</option>
                                                    <option value="VAT">Vatican City</option>
                                                    <option value="VEN">Venezuela</option>
                                                    <option value="VNM">Viet Nam</option>
                                                    <option value="UMI">UMI</option>
                                                    <option value="WLF">Wallis and Futuna</option>
                                                    <option value="ESH">Western Sahara</option>
                                                    <option value="YEM">Yemen</option>
                                                    <option value="ZMB">Zambia</option>
                                                    <option value="ZWE">Zimbabwe</option>
                                                </select>

                                            </p>

                                            <p>
                                                <label for="locale">Locale</label><span class="requiredField ">*</span><br/>
                                                <select name="locale" id="locale">
                                                    <option value="">Select One...</option>
                                                    <option value="en_GB" selected="selected">English (United Kingdom)</option>
                                                </select>

                                            </p>

                                            <p>
                                                <label for="timeZone">Time Zone</label><span class="requiredField ">*</span><br/>
                                                <select name="timeZone" id="timeZone">
                                                    <option value="">Select One...</option>
                                                    <option value="Etc/GMT-12">(GMT-12:00) International Date Line West</option>
                                                    <option value="Etc/GMT-11">(GMT-11:00) Midway Island, Samoa</option>
                                                    <option value="Etc/GMT-10">(GMT-10:00) Hawaii</option>
                                                    <option value="Etc/GMT-9">(GMT-09:00) Alaska</option>
                                                    <option value="Etc/GMT-8">(GMT-08:00) Pacific</option>
                                                    <option value="Etc/GMT-7">(GMT-07:00) Mountain</option>
                                                    <option value="Etc/GMT-6">(GMT-06:00) Central</option>
                                                    <option value="Etc/GMT-5">(GMT-05:00) Eastern</option>
                                                    <option value="Etc/GMT-4">(GMT-04:00) Atlantic</option>
                                                    <option value="Etc/GMT-3">(GMT-03:00) Greenland</option>
                                                    <option value="Etc/GMT-2">(GMT-02:00) Mid-Atlantic</option>
                                                    <option value="Etc/GMT-1">(GMT-01:00) Azores</option>
                                                    <option value="Etc/GMT0">(GMT) Greenwich</option>
                                                    <option value="Etc/GMT+1">(GMT+01:00) Central European</option>
                                                    <option value="Etc/GMT+2">(GMT+02:00) Eastern European</option>
                                                    <option value="Etc/GMT+3">(GMT+03:00) Moscow, Baghdad</option>
                                                    <option value="Etc/GMT+4">(GMT+04:00) Abu Dhabi, Dubai</option>
                                                    <option value="Etc/GMT+5" selected="selected">(GMT+05:00) Islamabad, Karachi</option>
                                                    <option value="Etc/GMT+6">(GMT+06:00) Astana, Dhaka</option>
                                                    <option value="Etc/GMT+7">(GMT+07:00) Bangkok, Jakarta</option>
                                                    <option value="Etc/GMT+8">(GMT+08:00) China Coast, Western Australia</option>
                                                    <option value="Etc/GMT+9">(GMT+09:00) Japan, Korea</option>
                                                    <option value="Etc/GMT+10">(GMT+10:00) Eastern Australia</option>
                                                    <option value="Etc/GMT+11">(GMT+11:00) Magadan, Solomon Is.</option>
                                                    <option value="Etc/GMT+12">(GMT+12:00) New Zealand, Fiji</option>
                                                </select>
                                            </p>

                                            <p>
                                                <label for="DOB">Date of Birth</label>
                                                <br/>
                                                <input name="DOB" value="date.struct" type="hidden"/>
                                                <select name="DOB_day" id="DOB_day">
                                                    <option value="" selected="selected">--</option>
                                                    <option value="1">1</option>
                                                    <option value="2">2</option>
                                                    <option value="3">3</option>
                                                    <option value="4">4</option>
                                                    <option value="5">5</option>
                                                    <option value="6">6</option>
                                                    <option value="7">7</option>
                                                    <option value="8">8</option>
                                                    <option value="9">9</option>
                                                    <option value="10">10</option>
                                                    <option value="11">11</option>
                                                    <option value="12">12</option>
                                                    <option value="13">13</option>
                                                    <option value="14">14</option>
                                                    <option value="15">15</option>
                                                    <option value="16">16</option>
                                                    <option value="17">17</option>
                                                    <option value="18">18</option>
                                                    <option value="19">19</option>
                                                    <option value="20">20</option>
                                                    <option value="21">21</option>
                                                    <option value="22">22</option>
                                                    <option value="23">23</option>
                                                    <option value="24">24</option>
                                                    <option value="25">25</option>
                                                    <option value="26">26</option>
                                                    <option value="27">27</option>
                                                    <option value="28">28</option>
                                                    <option value="29">29</option>
                                                    <option value="30">30</option>
                                                    <option value="31">31</option>
                                                </select>
                                                <select name="DOB_month" id="DOB_month">
                                                    <option value="" selected="selected">--</option>
                                                    <option value="1">January</option>
                                                    <option value="2">February</option>
                                                    <option value="3">March</option>
                                                    <option value="4">April</option>
                                                    <option value="5">May</option>
                                                    <option value="6">June</option>
                                                    <option value="7">July</option>
                                                    <option value="8">August</option>
                                                    <option value="9">September</option>
                                                    <option value="10">October</option>
                                                    <option value="11">November</option>
                                                    <option value="12">December</option>
                                                </select>
                                                <select name="DOB_year" id="DOB_year">
                                                    <option value="" selected="selected">--</option>
                                                    <option value="2010">2010</option>
                                                    <option value="2009">2009</option>
                                                    <option value="2008">2008</option>
                                                    <option value="2007">2007</option>
                                                    <option value="2006">2006</option>
                                                    <option value="2005">2005</option>
                                                    <option value="2004">2004</option>
                                                    <option value="2003">2003</option>
                                                    <option value="2002">2002</option>
                                                    <option value="2001">2001</option>
                                                    <option value="2000">2000</option>
                                                    <option value="1999">1999</option>
                                                    <option value="1998">1998</option>
                                                    <option value="1997">1997</option>
                                                    <option value="1996">1996</option>
                                                    <option value="1995">1995</option>
                                                    <option value="1994">1994</option>
                                                    <option value="1993">1993</option>
                                                    <option value="1992">1992</option>
                                                    <option value="1991">1991</option>
                                                    <option value="1990">1990</option>
                                                    <option value="1989">1989</option>
                                                    <option value="1988">1988</option>
                                                    <option value="1987">1987</option>
                                                    <option value="1986">1986</option>
                                                    <option value="1985">1985</option>
                                                    <option value="1984">1984</option>
                                                    <option value="1983">1983</option>
                                                    <option value="1982">1982</option>
                                                    <option value="1981">1981</option>
                                                    <option value="1980">1980</option>
                                                    <option value="1979">1979</option>
                                                    <option value="1978">1978</option>
                                                    <option value="1977">1977</option>
                                                    <option value="1976">1976</option>
                                                    <option value="1975">1975</option>
                                                    <option value="1974">1974</option>
                                                    <option value="1973">1973</option>
                                                    <option value="1972">1972</option>
                                                    <option value="1971">1971</option>
                                                    <option value="1970">1970</option>
                                                    <option value="1969">1969</option>
                                                    <option value="1968">1968</option>
                                                    <option value="1967">1967</option>
                                                    <option value="1966">1966</option>
                                                    <option value="1965">1965</option>
                                                    <option value="1964">1964</option>
                                                    <option value="1963">1963</option>
                                                    <option value="1962">1962</option>
                                                    <option value="1961">1961</option>
                                                    <option value="1960">1960</option>
                                                    <option value="1959">1959</option>
                                                    <option value="1958">1958</option>
                                                    <option value="1957">1957</option>
                                                    <option value="1956">1956</option>
                                                    <option value="1955">1955</option>
                                                    <option value="1954">1954</option>
                                                    <option value="1953">1953</option>
                                                    <option value="1952">1952</option>
                                                    <option value="1951">1951</option>
                                                    <option value="1950">1950</option>
                                                    <option value="1949">1949</option>
                                                    <option value="1948">1948</option>
                                                    <option value="1947">1947</option>
                                                    <option value="1946">1946</option>
                                                    <option value="1945">1945</option>
                                                    <option value="1944">1944</option>
                                                    <option value="1943">1943</option>
                                                    <option value="1942">1942</option>
                                                    <option value="1941">1941</option>
                                                    <option value="1940">1940</option>
                                                    <option value="1939">1939</option>
                                                    <option value="1938">1938</option>
                                                    <option value="1937">1937</option>
                                                    <option value="1936">1936</option>
                                                    <option value="1935">1935</option>
                                                    <option value="1934">1934</option>
                                                    <option value="1933">1933</option>
                                                    <option value="1932">1932</option>
                                                    <option value="1931">1931</option>
                                                    <option value="1930">1930</option>
                                                    <option value="1929">1929</option>
                                                    <option value="1928">1928</option>
                                                    <option value="1927">1927</option>
                                                    <option value="1926">1926</option>
                                                    <option value="1925">1925</option>
                                                    <option value="1924">1924</option>
                                                    <option value="1923">1923</option>
                                                    <option value="1922">1922</option>
                                                    <option value="1921">1921</option>
                                                    <option value="1920">1920</option>
                                                    <option value="1919">1919</option>
                                                    <option value="1918">1918</option>
                                                    <option value="1917">1917</option>
                                                    <option value="1916">1916</option>
                                                    <option value="1915">1915</option>
                                                    <option value="1914">1914</option>
                                                    <option value="1913">1913</option>
                                                    <option value="1912">1912</option>
                                                    <option value="1911">1911</option>
                                                    <option value="1910">1910</option>
                                                    <option value="1909">1909</option>
                                                    <option value="1908">1908</option>
                                                    <option value="1907">1907</option>
                                                    <option value="1906">1906</option>
                                                    <option value="1905">1905</option>
                                                    <option value="1904">1904</option>
                                                    <option value="1903">1903</option>
                                                    <option value="1902">1902</option>
                                                    <option value="1901">1901</option>
                                                    <option value="1900">1900</option>
                                                </select>
                                            </p>		    

                                            <p><label for="website">Website</label><br>
                                                    <input name="website" id="website" type="text"/>

                                            </p>

                                            <p>
                                                <label for="twitter">Twitter</label><br>
                                                    <input name="twitter" id="twitter" type="text"/>
                                            </p>

                                            <h2>Options</h2>
                                            <p><input name="_receiveCommunications" type="hidden"/><input name="receiveCommunications" id="receiveCommunications" type="checkbox"/>
                                                <label for="receiveCommunications">Receive email communications from MIFE?</label></p>

                                        </div>

                                        <div class="grid_6 omega">
                                            <h2>Community</h2>

                                            <div class="clearfix">
                                                <label>&nbsp;</label><br>
                                                    <div class="grid_1 alpha">
                                                        <img id="avatarImage" alt="null" class="[:]" src="template/default_avatar.png" height="80" width="80"><br>



                                                                </div>
                                                                <div class="grid_4 omega">
                                                                    <label><input name="avatarType" value="CUSTOM" id="avatarType" type="radio"/>Use Custom</label><br/>
                                                                    <div id="uploadCustom" style="display: none;"><input id="customAvatarFile" name="customAvatarFile" type="file"/></div>
                                                                    <label><input name="avatarType" value="GRAVATAR" class="nonCustomAvatar" id="avatarType" type="radio"/><a href="http://www.gravatar.com/" target="_blank" alt="Visit Gravatar">Use Gravatar</a></label><br/>
                                                                    <label><input name="avatarType" checked="checked" value="DEFAULT" class="nonCustomAvatar" id="avatarType" type="radio"/>Use Default</label>

                                                                </div>
                                                                </div>

                                                                <br class="spacer">

                                                                    <p><label for="displayName">Display Name</label><span class="requiredField ">*</span><br/>
                                                                        <input id="displayName" name="displayName" class="wide" value="Roshan" type="text"/>

                                                                    </p>

                                                                    <p>
                                                                        <label for="signature">Signature</label><br/>
                                                                        <textarea id="signature" name="signature" rows="6" cols="40"></textarea>

                                                                        <br/>
                                                                    </p><h2>Professional</h2>
                                                                    <p><label for="company">Company Name</label><br/>
                                                                        <input name="company" id="company" type="text"/></p>
                                                                    <p><label for="jobTitle">Job Title</label><br/>
                                                                        <input name="jobTitle" id="jobTitle" type="text"/></p>

                                                                    <p><label for="jobFunction">Job Function</label><br/>
                                                                        <select name="jobFunction" id="jobFunction">
                                                                            <option selected="selected" value="technical">Technical</option>
                                                                            <option value="commercialMarketing">Commercial / Marketing</option>
                                                                            <option value="sales">Sales</option>
                                                                            <option value="iDoEverything">I do everything</option>
                                                                        </select>
                                                                    </p>
                                                                    </div>

                                                                    </div>

                                                                    <div class="grid_12">
                                                                        <p align="right"><button type="submit">Update Profile</button> or <a href="index.jsp">Cancel</a></p>
                                                                    </div>
                                                                    </form>
                                                                    </div>
                                                                    <script type="text/javascript">
                                                                        $().ready(function() {
                                                                            jQuery.validator.addMethod("validateDate", function(value, element, param) {
                                                                                var userDate = new Date();
                                                                                var params = [
                                                                                    $(param + '_day').val(),
                                                                                    $(param + '_month').val(),
                                                                                    $(param + '_year').val()
                                                                                ];
                                                                                if (params[0] == "" && params[1] == "" && params[2] == "") {
                                                                                    return true;
                                                                                }
                                                                                userDate.setFullYear(parseInt(params[2]), parseInt(params[1]) - 1, parseInt(params[0]));
                                                                                return (userDate.getMonth()+1 == params[1]) &&
                                                                                    (userDate.getDate() == params[0]) &&
                                                                                    (userDate.getFullYear() == params[2]) &&
                                                                                    (new Date() >= userDate);
                                                                            });
                                                                        });
                                                                    </script>
                                                                    <script type="text/javascript">
                                                                        $().ready(function() {

                                                                            $('[value=CUSTOM]').click(function() {
                                                                                $("#uploadCustom").show();
                                                                                $("#uploadNewCustom").hide;
                                                                            });

                                                                            function showUploadField() {
                                                                                if($('[value=CUSTOM]').attr('checked') == true) {
                                                                                    $("#uploadCustom").show();          
                                                                                }
                                                                            }

                                                                            $(".nonCustomAvatar").click(function() {
                                                                                $("#uploadCustom").hide();
                                                                                $("#uploadNewCustom").show();
                                                                            })
	
                                                                            $("#userProfileForm").validate({
                                                                                errorPlacement: function(error, element) {
                                                                                    if (element.attr('type') == 'checkbox') {
                                                                                        error.appendTo(element.parent().parent());
                                                                                    } else {
                                                                                        error.appendTo(element.parent());
                                                                                    }
                                                                                },
                                                                                success: function(label) {
                                                                                    if (label.attr('for') == 'newEmail' || label.attr('for') == 'displayName') {
                                                                                        label.addClass("success");
                                                                                    }
                                                                                },
                                                                                submitHandler: function(form) {
                                                                                    if(typeof theLoader == "function"){
                                                                                        theLoader();
                                                                                    }
                                                                                    form.submit();
                                                                                },
                                                                                rules: {
                                                                                    firstName: "required",
                                                                                    lastName: "required",
                                                                                    displayName: {
                                                                                        required: true,
                                                                                        minlength: 4,
                                                                                        remote: '/g/user/availability'
                                                                                    },
                                                                                    email: {
                                                                                        required: true,
                                                                                        email: true
                                                                                    },
                                                                                    newEmail: {
                                                                                        required: true,
                                                                                        email: true,
                                                                                        remote: '/g/user/availability'
                                                                                    },
                                                                                    country: "required",
                                                                                    uploadCustom: {
                                                                                        required: '[value=CUSTOM]:checked'
                                                                                    },
                                                                                    DOB_year : {
                                                                                        validateDate : "select#DOB"
                                                                                    },
                                                                                    website: {
                                                                                        url: true
                                                                                    },
                                                                                    locale: "required",
                                                                                    timeZone: "required",
                                                                                    signature: {
                                                                                        maxlength: 255
                                                                                    }
        
                                                                                },
                                                                                messages: {
                                                                                    firstName: "Please enter your first name",
                                                                                    lastName: "Please enter your last name",
                                                                                    displayName: {
                                                                                        required: "Please enter a display name",
                                                                                        minlength: "Your display name must consist of at least 4 characters",
                                                                                        remote: "The display name you entered is not available"			  
                                                                                    },
                                                                                    email: "Please enter a valid email address",
                                                                                    newEmail: {
                                                                                        required: "Please enter your email address",
                                                                                        email: "Please enter a valid email address",
                                                                                        remote: "The email address you entered is unavailable"
                                                                                    },
                                                                                    country: "Please select a country",
                                                                                    DOB_year : {
                                                                                        validateDate : "Please enter a valid date of birth"
                                                                                    },
                                                                                    website : {
                                                                                        url : "Please enter a valid URL"
                                                                                    },
                                                                                    locale: "Please select a locale",
                                                                                    timeZone: "Please select a time zone",
                                                                                    signature: {
                                                                                        maxlength: "Your signature must not exceed 255 characters"
                                                                                    }
        
                                                                                }
                                                                            });
                                                                        });
                                                                    </script>

                                                                    </div>
                                                                    <div id="clearfooter"></div>
                                                                    </div>

                                                                    <div id="footer">
                                                                        <div id="footer_container" class="container_12">
                                                                            <div class="grid_12">




                                                                                <div class="nav-footer"><ul></ul></div>


                                                                                <div id="logos">

                                                                                </div>

                                                                                <div id="copyright">© 2012 MIFE</div>


                                                                                <div class="sub-nav-footer"><ul><li><a href="http://mife.com/g/sitemap/index">Site Map</a></li><li><a href="http://mife.com/g/termsAndConditions/show">Terms &amp; Conditions</a></li><li><a href="http://www.gsma.com/legal/">Legal</a></li><li><a href="http://www.aepona.com/"></a><a href="http://www.aepona.com/" id="menu-Aepona"></a></li></ul></div>


                                                                                <div class="clear">&nbsp;</div>
                                                                            </div>
                                                                        </div>

                                                                    </div>


                                                                    </body>
                                                                    </html>

<%
    if(request.getSession().getAttribute("user_profile_id") != null){
        response.sendRedirect("profile.jsp");
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>Registration Form | MIFE Sandbox Portal</title>
        <%@include file="commons/common_head.jsp" %>
    </head>
    <body>
        <div id="fullContainer">
            <%@include file="commons/header.jsp" %>
            <div class="container_12">
                <div class="grid_12">


                    <!-- Comment needed for IE7 -->
                </div>
            </div>

            <div id="body_container" class="container_12 clearfix">
                <div class="grid_12">
                    <h2>Registration Form</h2>
                </div>

                <div class="grid_10 prefix_1 suffix_1">
                </div>

                <div id="inputArea">
                    <div class="grid_12">
                        <form action="UserRegistration" method="POST" name="user_registration" id="user_registration">

                            <div class="grid_6 alpha">
                                <p>
                                    <label for="firstName">First Name</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <input id="firstName" name="first_name" type="text" required="required"/>

                                </p>
                            </div>

                            <div class="grid_6 omega">
                                <p>
                                    <label for="lastName">Last Name</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <input id="lastName" name="last_name" type="text" required="required"/>

                                </p>
                            </div>

                            <div class="grid_6 suffix_6 alpha">
                                <p>
                                    <label for="newEmail">Email</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <input id="newEmail" name="email" type="text" required="required"/>
                                </p>
                            </div>

                            <div class="grid_6 suffix_6 alpha">
                                <p>
                                    <label for="partnerName">Partner Name</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <input id="partnerName" name="partner_name" type="text" required="required"/>
                                </p>
                            </div>

                            <div class="grid_6 suffix_6 alpha">
                                <p>
                                    <label for="currency">Currency</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <select name="currency" id="currency">
                                        <option selected="selected" value="" required="required"> Select One...</option>
                                        <option value="USD">USD</option>
                                    </select>

                                </p>
                            </div>

                            <div class="grid_6 suffix_6 alpha">
                                <p>
                                    <label for="telephone">Telephone</label>
                                    <br/>
                                    <input id="telephone" name="telephone_no" type="text" />

                                </p>

                            </div>


                            <div class="grid_6 suffix_6 alpha">
                                <p>
                                    <label for="displayName">Display Name</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <input id="displayName" name="display_name" type="text"  required="required"/>
                                </p>     
                            </div>   
                            <div class="grid_6 alpha">
                                <p>
                                    <label for="password">Password</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <input id="password" name="password" value="" type="password"  required="required"/>
                                </p>
                            </div>

                            <div class="grid_6 omega">
                                <p>
                                    <label for="confirmPassword">Confirm Password</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <input id="confirmPassword" name="confirm_password" value="" type="password"  required="required"/>

                                </p>
                            </div>
                            <div class="grid_6 suffix_6 alpha">
                                <p>
                                    <label for="securityQuestion">Security Question</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <select name="security_question" id="securityQuestion" required="required">
                                        <option selected="selected" value="">Select One...</option>
                                        <option value="What was your town/city of birth?">What was your town/city of birth?</option>
                                    </select>

                                </p>
                            </div>

                            <div class="grid_6 alpha">
                                <p>
                                    <label for="securityResponse">Security Response</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <input id="securityResponse" name="security_response" value="" type="password"  required="required" />

                                </p>
                            </div>

                            <div class="grid_6 omega">
                                <p>
                                    <label for="confirmSecurityResponse">Confirm Security Response</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <input id="confirmSecurityResponse" name="confirm_security_response" value="" type="password"  required="required"/>

                                </p>
                            </div> 
                            <div class="grid_4 alpha">
                                <p>
                                    <label for="country">Country</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <select name="country" id="country" required="required">
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
                                        <option value="LKA">Sri Lanka</option>
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
                                        <option value="GBR" selected="selected">United Kingdom</option>
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
                            </div>

                            <div class="grid_4">
                                <p>
                                    <label for="locale">Locale</label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                    <select name="locale" id="locale" required="required">
                                        <option value="">Select One...</option>
                                        <option value="en_GB" selected="selected">English (United Kingdom)</option>
                                    </select>

                                </p>
                            </div>

                            <div class="grid_4 alpha">
                                <p>
                                    <label for="timeZone">Time Zone</label>
                                    <span class="requiredField ">*</span><br/>
                                    <select name="time_zone" id="timeZone" required="required">
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
                                        <option value="Etc/GMT0" selected="selected">(GMT) Greenwich</option>
                                        <option value="Etc/GMT+1">(GMT+01:00) Central European</option>
                                        <option value="Etc/GMT+2">(GMT+02:00) Eastern European</option>
                                        <option value="Etc/GMT+3">(GMT+03:00) Moscow, Baghdad</option>
                                        <option value="Etc/GMT+4">(GMT+04:00) Abu Dhabi, Dubai</option>
                                        <option value="Etc/GMT+5">(GMT+05:00) Islamabad, Karachi</option>
                                        <option value="Etc/GMT+6">(GMT+06:00) Astana, Dhaka</option>
                                        <option value="Etc/GMT+7">(GMT+07:00) Bangkok, Jakarta</option>
                                        <option value="Etc/GMT+8">(GMT+08:00) China Coast, Western Australia</option>
                                        <option value="Etc/GMT+9">(GMT+09:00) Japan, Korea</option>
                                        <option value="Etc/GMT+10">(GMT+10:00) Eastern Australia</option>
                                        <option value="Etc/GMT+11">(GMT+11:00) Magadan, Solomon Is.</option>
                                        <option value="Etc/GMT+12">(GMT+12:00) New Zealand, Fiji</option>
                                    </select>

                                </p>
                            </div>

                            <!--
                            <div class="grid_10 alpha">
                                <div class="clearfix">
                                    <p>
                                        <label for="captchaResponse">Enter this text:</label>
                                        <span class="requiredField ">*</span><br clear="all"/>
                                        <span class="floatableFormColumn">
                                            <img id="registrationCaptcha" src="/mifesandbox/template/registrationCaptcha.jpg"/>
                                        </span>

                                        <span class="floatableFormColumn">
                                            <input id="captchaResponse" name="captchaResponse" class="small" type="text" />
                                            <a href="/mifesandbox/g/user/register.gsp#" id="refreshCaptcha">Refresh Image</a><br/>

                                        </span>
                                    </p>
                                </div>
                            </div>
                            -->
                            
                            <div class="grid_12">
                                <h3><label>Terms and Conditions</label></h3>
                                <div id="terms-small">
                                    <table align="left" cellpadding="0" cellspacing="0">
                                        <tbody>
                                            <tr>
                                                <td><br/></td>
                                                <td><br/></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <p>&nbsp;</p>
                                    <p>For a list of Operators and their API usage Terms and Conditions please go to the following link:</p>
                                    <p><a href="#">#</a></p>
                                    <p>&nbsp;</p>
                                    <p>Version 1.0</p>
                                    <p>Copyright © 2012 MIFE</p>
                                </div>

                                <p>
                                    <label class="checkbox" for="termsConditionsAgreement">
                                        <input name="_termsConditionsAgreement" type="hidden"/>
                                        <input name="termsConditionsAgreement" id="termsConditionsAgreement" type="checkbox"/>I accept the <a href="/mifesandbox/g/termsAndConditions/show" target="_blank">Terms and Conditions</a>
                                    </label>
                                    <span class="requiredField ">*</span>
                                    <br/>
                                </p> 
                            </div>
                            <div class="grid_12">
                                <hr/>
                                <p align="right">
                                    <button type="submit" class="primary">Register</button>
                                </p>
                            </div>
                        </form>
                    </div>
                </div>

            </div>
            <div id="clearfooter"></div>
        </div>
        <%@include file="commons/footer.jsp" %>
    </body>
</html>

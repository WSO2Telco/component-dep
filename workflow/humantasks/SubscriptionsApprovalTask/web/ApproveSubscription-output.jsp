<script type="text/javascript">
createTaskOutput = function() {
    var outputVal = getCheckedRadio();
    if(outputVal == 'approve') {
		return '<sch:SubscriptionApprovalResponse xmlns:sch="http://org.wso2.carbon/subscriptions/schema"><sch:approved>true</sch:approved></sch:SubscriptionApprovalResponse>';
	} else if (outputVal == 'disapprove') {
		return '<sch:SubscriptionApprovalResponse xmlns:sch="http://org.wso2.carbon/subscriptions/schema"><sch:approved>false</sch:approved></sch:SubscriptionApprovalResponse>';
	}
};

getCheckedRadio = function () {
      var radioButtons = document.getElementsByName("responseRadio");
      for (var x = 0; x < radioButtons.length; x ++) {
        if (radioButtons[x].checked) {
          return radioButtons[x].value;
        }
      }
    };
</script>

<p>
<form>
<table border="0">
    <tr>
	<td>
<input type="radio" name="responseRadio" id="responseRadio1" value="approve" /> Approve
<input type="radio" name="responseRadio" id="responseRadio2" value="disapprove" /> Disapprove 
    </td>
    </tr>

</table>
</form>
</p>

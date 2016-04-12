$(document).ready(function () {
    setTablePagination(0);
    getLocationSessionData();
});

function createAuthorizationCode() {
    var action = "createAuthCodeRequest";

    var key = $("#key").val();
    var secret = $("#secret").val();

    if ((jQuery.trim(key).length > 0) && (jQuery.trim(secret).length > 0)) {
        jagg.post("/site/blocks/authorization-api/ajax/authorization-api.jag", {
            action: action,
            key: key,
            secret: secret

        }, function (result) {
            if (!result.error) {
                if (result.data != "null") {
                    $('#lbsAddMessage').show();
                    $('#lbsAddMessage').delay(4000).hide('fast');
                    $('#authCode').val(result.data);
                    $('#auth_header').val('Basic ' + result.data);
                    $('#token').val(result.data);
                    //window.location.reload();
                } else {
                    $('#lbsErrorMessage').show();
                    $('#lbsErrorMessage').delay(4000).hide('fast');
                }
            } else {
                $('#lbsErrorMessage').show();
                $('#lbsErrorMessage').delay(4000).hide('fast');
            }
        }, "json");
    }
    else {
        document.getElementById("error_message").innerHTML = "Please enter a valid  Consumer Key and a  Consumer Secret to proceed";
    }

};

var reqUrl;

function saveRefreshToken() {
    var action = "generateRefreshToken";

    var granttype = $("#granttype").val();
    var username = $("#username").val();
    var password = $("#password").val();
    var scope = $("#scope").val();
    var token = $("#token").val();


    jagg.post("/site/blocks/authorization-api/ajax/authorization-api.jag", {
        action: action,
        granttype: granttype,
        username: username,
        password: password,
        scope: scope,
        token: token

    }, function (result) {


        reqUrl = 'https://ideabiz.lk/apicall/token?grant_type=' + granttype + '&username=' + username + '&password=' + password + '&scope=' + scope

        $("#granttype").val('');
        $("#username").val('');
        $("#password").val('');
        $("#scope").val('');
        $("#token").val('');

        $("#url").val(reqUrl);

        if (!result.error) {
            if (result.data != "null") {
                $('#lbsAddMessage').show();
                $('#lbsAddMessage').delay(4000).hide('fast');
                window.location.reload();
            } else {
                $('#lbsErrorMessage').show();
                $('#lbsErrorMessage').delay(4000).hide('fast');
            }
            setTablePagination(0);
        } else {
            $('#lbsErrorMessage').show();
            $('#lbsErrorMessage').delay(4000).hide('fast');
        }
    }, "json");
};


function sendMobileIdApiRequestWithToken(authorization) {
    var action = "sendMobileIdApiRequestWithToken";

    jagg.post("/site/blocks/authorization-api/ajax/authorization-api.jag", {
        action: action,
        authorization: authorization

    }, function (result) {
        if (!result.error) {
            if (result.data != "null") {
                $('#json-response').val(result.data);
                $("#mobileidapi_table_content").empty();
                //createMobileIdApiTable(result.table);
            } else {
                $('#errorMessage').show();
                $('#errorMessage').delay(4000).hide('fast');
            }
            setTablePagination(0);
        } else {
            $('#errorMessage').show();
            $('#errorMessage').delay(4000).hide('fast');
        }
    }, "json");
};

function setTablePagination(pageNumber) {
    paginator(pageNumber);
}

function paginator(pageNumber) {
    var rows = $("#loc_request_table tbody tr").length;
    var rowsPerPage = 10;
    if (rows > rowsPerPage) {
        var numberOfPages = Math.ceil(rows / rowsPerPage);
        var currentPageStart = pageNumber * rowsPerPage;
        var currentPageEnd = (pageNumber * rowsPerPage) + rowsPerPage;
        for (var i = 0; i < rows; i++) {
            if ((currentPageStart <= i) & (i < currentPageEnd)) {
                $("#loc_request_table tr").eq(i).show();
            } else {
                $("#loc_request_table tbody tr").eq(i).hide();
            }
        }
        loadPaginatorView(numberOfPages, pageNumber);
    } else {
        $(".pagination").html('');
    }
}

function loadPaginatorView(numberOfPages, currentPage) {
    $(".pagination").html('<ul></ul>');
    var previousAppender = '<li><a href="javascript:paginator(0)"><<</a></li>';
    if (currentPage == 0) {
        previousAppender = '<li class="disabled"><a><<</a></li>';
    }
    $(".pagination ul").append(previousAppender);
    for (var i = 0; i < numberOfPages; i++) {
        var currentRow;
        var rowSticker = i + 1;
        if (i == currentPage) {
            currentRow = '<li class="active"><a>' + rowSticker + '</a></li>';
        } else {
            currentRow = '<li><a href="javascript:paginator(' + i + ')">' + rowSticker + '</a></li>';
        }
        $(".pagination ul").append(currentRow);
    }
    var lastPage = numberOfPages - 1;
    var postAppender = '<li><a href="javascript:paginator(' + lastPage + ')">>></a></li>';
    if (currentPage == lastPage) {
        postAppender = '<li class="disabled"><a>>></a></li>';
    }
    $(".pagination ul").append(postAppender);
}


function generateRefreshToken() {
    var action = "getRefreshToken";

    var url = $("#url").val();

    jagg.post("/site/blocks/authorization-api/ajax/authorization-api.jag", {
        action: action,
        url: url

    }, function (result) {

        var json = result.data,
            obj = JSON.parse(json);
        obj.MobileIdApiRequest.refreshToken;
        $('#refresh_token').val(obj.MobileIdApiRequest.refreshToken);

        $('#json-response').val(result.data);
        if (!result.error) {
            if (result.data != "null") {
                $('#lbsAddMessage').show();
                $('#lbsAddMessage').delay(4000).hide('fast');
                window.location.reload();
            } else {
                $('#lbsErrorMessage').show();
                $('#lbsErrorMessage').delay(4000).hide('fast');
            }
            setTablePagination(0);
        } else {
            $('#lbsErrorMessage').show();
            $('#lbsErrorMessage').delay(4000).hide('fast');
        }
    }, "json");
};

function generateAccessToken() {
    var action = "generateAccessTokenRequest";

    var grant_type = $("#grant_type").val();
    var refresh_token = $("#refresh_token").val();
    var scope = $("#token_scope").val();
    var auth_code = $("#authCode").val();

    if ((jQuery.trim(grant_type).length > 0) && (jQuery.trim(refresh_token).length > 0) &&
        (jQuery.trim(scope).length > 0) && (jQuery.trim(auth_code).length > 0)) {

        jagg.post("/site/blocks/authorization-api/ajax/authorization-api.jag", {
            action: action,
            grant_type: grant_type,
            refresh_token: refresh_token,
            scope: scope,
            auth_code: auth_code

        }, function (result) {
            if (!result.error) {
                if (result.data != "null") {
                    $('#lbsAddMessage').show();
                    $('#lbsAddMessage').delay(4000).hide('fast');
                    $('#json-response-token').val(result.data);
                    var url = $('#access_token_url').val() + grant_type + '&refresh_token=' + refresh_token + '&scope=' + scope;
                    $('#access_token_url').val(url);
                    window.location.reload();
                } else {
                    $('#lbsErrorMessage').show();
                    $('#lbsErrorMessage').delay(4000).hide('fast');
                }
            } else {
                $('#lbsErrorMessage').show();
                $('#lbsErrorMessage').delay(4000).hide('fast');
            }
        }, "json");
    }
    else {
        document.getElementById("access_token_error_message").innerHTML = "Please enter a valid  grant type and a  Scope to proceed";
    }
};
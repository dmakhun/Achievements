<%@ include file="../libs/libs.jsp" %>
<script type="text/javascript" src="resources/script/jquery.js"></script>
<script type="text/javascript">
	function onLoad() {

		$("#password").keyup(checkPasswordsMatch);
		$("#confirmpass").keyup(checkPasswordsMatch);
		$("#details").submit(canSubmit);
	}

	function canSubmit() {
		var password = $("#password").val();
		var confirmpass = $("#confirmpass").val();

		if (password != confirmpass) {
			alert("<fmt:message key='UnmatchedPasswords.user.password'/>");
			return false;
		} else {
			return true;
		}
	}

	function checkPasswordsMatch() {
		var password = $("#password").val();
		var confirmpass = $("#confirmpass").val();

		if (password.length >= 4 && confirmpass.length >= 1) {
			$("#matchpass").css("display", "inherit");
			if (password == confirmpass) {
				$("#matchpass").text(
						"<fmt:message key='MatchedPasswords.user.password'/>");
				$("#matchpass").addClass("alert-success");
				$("#matchpass").removeClass("alert-danger");
			} else {
				$("#matchpass")
						.text(
								"<fmt:message key='UnmatchedPasswords.user.password'/>");
				$("#matchpass").addClass("alert-danger");
				$("#matchpass").removeClass("alert-success");
			}
		}

		if (password.length == 0) {
			$("#matchpass").css("display", "none");
		}
	}
	$(document).ready(onLoad);


</script>

<sf:form method="post" action="createaccount" id="details"
         modelAttribute="user">


    <div class="row">
        <div class="col-md-offset-3 col-md-6 text-center">
            <h1>
                <spring:message code="menu.3tab"/>
            </h1>
        </div>
    </div>

    <div class="row">
        <div class="col-md-offset-3 col-md-2">
            <p>
                <spring:message code="registration.login"/>
            </p>

        </div>
        <div class="col-md-4">
            <div class="form-group">
                <sf:input id="username" path="username" name="username" type="text"
                          cssClass="form-control"/>
            </div>
        </div>

        <div class="col-md-offset-3 col-md-6 text-center">
            <sf:errors element="div" cssClass="alert alert-danger"
                       path="username"></sf:errors>
        </div>
    </div>

    <div class="row">
        <div class="col-md-offset-3 col-md-2">
            <p>
                <spring:message code="registration.firstname"/>
            </p>
        </div>
        <div class="col-md-4">
            <div class="form-group">
                <sf:input path="name" name="name" type="text"
                          cssClass="form-control"/>
            </div>
        </div>
        <div class="col-md-offset-3 col-md-6 text-center">
            <sf:errors element="div" cssClass="alert alert-danger" path="name"></sf:errors>
        </div>
    </div>

    <div class="row">
        <div class="col-md-offset-3 col-md-2">
            <p>
                <spring:message code="registration.secondname"/>
            </p>
        </div>
        <div class="col-md-4">
            <div class="form-group">
                <sf:input id="username" path="surname" name="surname" type="text"
                          cssClass="form-control"/>
            </div>
        </div>
        <div class="col-md-offset-3 col-md-6 text-center">
            <sf:errors element="div" cssClass="alert alert-danger" path="surname"></sf:errors>
        </div>
    </div>

    <div class="row">
        <div class="col-md-offset-3 col-md-2">
            <p>
                <spring:message code="registration.email"/>
            </p>
        </div>

        <div class="col-md-4">
            <div class="form-group">
                <sf:input id="email" path="email" name="email" type="text"
                          cssClass="form-control"/>
            </div>
        </div>
        <div class="col-md-offset-3 col-md-6 text-center">
            <sf:errors element="div" cssClass="alert alert-danger" path="email"></sf:errors>
        </div>
    </div>

    <div class="row">
        <div class="col-md-offset-3 col-md-2">
            <p>
                <spring:message code="registration.password"/>
            </p>
        </div>
        <div class="col-md-4">
            <div class="form-group">
                <sf:input id="password" path="password" name="password"
                          type="password" cssClass="form-control"/>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-offset-3 col-md-2">
            <p>
                <spring:message code="registration.confirm"/>
            </p>
        </div>
        <div class="col-md-4">
            <div class="form-group">
                <input id="confirmpass" name="confirmpass" type="password"
                       class="form-control"/>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-offset-5 col-md-4">
            <div id="matchpass" class="stack-aside alert" style="display: none;"></div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-offset-3 col-md-6 text-center">
            <sf:errors element="div" cssClass="alert alert-danger"
                       path="password"></sf:errors>
        </div>
    </div>

    <div class="row">
        <div class="col-md-offset-5 col-md-4">
            <button type="submit" class="btn btn-info">
                <spring:message code="registration.done"/>
            </button>
        </div>
    </div>
</sf:form>

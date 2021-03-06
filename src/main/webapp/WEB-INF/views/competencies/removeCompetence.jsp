<%@ include file="../libs/libs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<div class="container">
    <center>
        <h1>
            <spring:message code="menu.9tab5"/>
        </h1>
    </center>
    <br/>
    <c:if test="${status == 'success'}">
        Competence removed!
    </c:if>
    <form action="" method="POST">
        <div class="row text-center">
            <div class="col-md-offset-4 col-md-4">
                <div class="form-group">
                    <select class="selectpicker" name="competence">
                        <option selected value="SelectBranch"><spring:message
                                code="select.branch"/></option>
                        <c:forEach var="item" items="${competenceList}">
                            <option value="${item.id}">${item.name}</option>
                        </c:forEach>
                    </select> &nbsp
                    <div class="btn-group btn-group-lg">
                        <button class="btn btn-success" type="submit"
                                name="removeCompetence">
                            <spring:message code="remove"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </form>

</div>
</body>
</html>
<%@ include file="../libs/libs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<div class="container">
    <center>
        <h1>
            <spring:message code="menu.4tab5"/>
        </h1>
    </center>
    <br/>
    <c:if test="${status == 'success'}">
        Achievement Type removed!
    </c:if>
    <form action="" method="post">
        <br>
        <div class="row text-center">
            <div class="col-md-offset-4 col-md-4">
                <div class="form-group">
                    <select class="selectpicker" name="AchievmentTypeList">
                        <option selected value="SelectAchievmentType"><spring:message
                                code="select.achievmentType"/></option>
                        <c:forEach var="item" items="${achievementTypeList}">
                            <option value="${item.id}">${item.name}</option>
                        </c:forEach>
                    </select>
                    &nbsp
                    <div class="btn-group btn-group-lg">
                        <button class="btn btn-success" type="submit"
                                name="removeAchievmentType">
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
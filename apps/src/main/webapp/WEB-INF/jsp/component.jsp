<%@include file="header.jsp"%>
<h3>Component details</h3>
<table class="table">
<tr><td>ID</td><td><c:out value="${id}" /></td></tr>
<tr><td>Name</td><td><c:out value="${name}" /></td></tr>
<tr><td> Description </td><td><c:out value="${description}"/></td></tr>
</table>
<!-- custom message field.  -->

<p><i>Use the following text field to send a custom message to this component.</i></p>

<div class="input-append">
  <form>  
  <input type="hidden" name="componentId" value="${id}"/>
  <input class="span2" style="width: 500px;" id="appendedInputButton" name="msg" type="text"/>
  <button type="submit" class="btn btn-primary">Send!</button>
  </form>
</div>

<hr />
<%@include file="footer.jsp"%>
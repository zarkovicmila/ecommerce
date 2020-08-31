function ConfirmForm() {
	$("#BlockUIConfirm").show();
}

function Submit() {
  $('#BlockUIConfirm').hide();
}

$(document).ready(function(){
  $(".dropdown-trigger").dropdown();
}); 

function SuccessAlert() {
	$("#SuccessCheckout").show();
}



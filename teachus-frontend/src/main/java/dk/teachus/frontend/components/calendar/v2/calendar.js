$(window).resize(layoutCalendar);

function layoutCalendar() {
	$(".bodytable table").height($(".bodytable .timelabel").size() * $(".bodytable .timelabel").height());
	
	//$(".newdaytimelesson").cluetip({local: true, showTitle: false, positionBy:'auto', arrows: true});
	
	var footer = $("#footer");
	var bodyTable = $(".bodytable");
	var contentStartY = bodyTable.offset().top;
	var lowestY = footer.offset().top+footer.outerHeight();
	var windowHeight = $(window).height();
	var tableHeight = bodyTable.height() + windowHeight - lowestY;
	bodyTable.height(tableHeight);
}

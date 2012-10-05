$(window).resize(layoutCalendar);

function layoutCalendar() {
	$(".bodytable table").height($(".bodytable .timelabel").size() * $(".bodytable .timelabel").height());
	
	var footer = $("footer");
	var bodyTable = $(".bodytable");
	var contentStartY = bodyTable.offset().top;
	var lowestY = footer.offset().top+footer.outerHeight()+50;
	var windowHeight = $(window).height();
	var tableHeight = bodyTable.height() + windowHeight - lowestY;
	bodyTable.height(tableHeight);
}

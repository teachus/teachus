$('body').tooltip({
    selector: '[rel=tooltip]'
});

$('.popover-external').popover({
	trigger : 'hover',
	content : function() {
		var $element = $(this);
		var content = $($element.data('content-id')).html();
		return content;
	},
	placement : 'right'
});

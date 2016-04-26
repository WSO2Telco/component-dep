$(document).ready(function() {

	$('.nav-header>ul>li>a').mouseover(fn)(function() {
		if($($(this).parent()).find('ul').length > 0 ){
			$('.nav-header>ul>li>ul').slideUp('fast');
			$($(this).parent()).find('ul').slideDown();
		
			return false;
		}
	});

	$('.nav-header>ul>li>ul>li').mouseover(function() {
		$('.nav-header>ul>li>ul').slideUp('fast');
	});

	$('.nav-header>ul>li>ul').mouseover(function() {
		$('.nav-header>ul>li>ul').slideUp('fast');
	});
});

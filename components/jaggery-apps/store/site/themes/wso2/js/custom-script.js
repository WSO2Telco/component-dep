/*================================================================================
	Item Name: Materialize - Material Design Admin Template
	Version: 3.0
	Author: GeeksLabs
	Author URL: http://www.themeforest.net/user/geekslabs
================================================================================

NOTE:
------
PLACE HERE YOUR OWN JS CODES AND IF NEEDED.
WE WILL RELEASE FUTURE UPDATES SO IN ORDER TO NOT OVERWRITE YOUR CUSTOM SCRIPT IT'S BETTER LIKE THIS. */


   <!-- Toast Notification -->
            // Toast Notification
            $(window).load(function() {

                "use strict";

                setTimeout(function() {
                    Materialize.toast('<span>Hiya! I am a toast.</span>', 1500);
                }, 1500);
                setTimeout(function() {
                    Materialize.toast('<span>You can swipe me too!</span>', 3000);
                }, 5000);
                setTimeout(function() {
                    Materialize.toast('<span>You have new order.</span><a class="btn-flat yellow-text" href="#">Read<a>', 3000);
                }, 15000);
            });


var $slide = $('.slide'),
    $slideGroup = $('.slide-group'),
    $bullet = $('.bullet');

var slidesTotal = ($slide.length - 1),
    current = 0,
    isAutoSliding = true;

$bullet.first().addClass('current');

var clickSlide = function() {
    //stop auto sliding
    window.clearInterval(autoSlide);
    isAutoSliding = false;

    var slideIndex = $bullet.index($(this));

    updateIndex(slideIndex);
};

var updateIndex = function(currentSlide) {
    if(isAutoSliding) {
        if(current === slidesTotal) {
            current = 0;
        } else {
            current++;
        }
    } else {
        current = currentSlide;
    }

    $bullet.removeClass('current');
    $bullet.eq(current).addClass('current');

    transition(current);
};

var transition = function(slidePosition) {
    $slideGroup.animate({
        'top': '-' + slidePosition + '00%'
    });
};

$bullet.on( 'click', clickSlide);

var autoSlide = window.setInterval(updateIndex, 2000);



/*добавление или удаление класса active при нажатии на меню-бургер*/
$(document).ready(function(){
   $('.menu_burger').click(function(event){
        $('.menu_burger,.menu_body').toggleClass('active');
       /*чтобы при открытом меню-бургере наш текст под ним не скролился*/
        $('body').toggleClass('lock');
                           });
    
   /* работа с меню-спойлерами в footer(показать/скрыть текст)*/
    $('.footer_block_spoiler_menu_title').click(function(event){
        
    //
      if($('.footer_block_menu').hasClass('one_spoiler_open')){
          $('.footer_block_spoiler_menu_title').not($(this)).removeClass('active');
          $('.footer_block_spoiler_menu_text').not($(this).next()).slideUp(300);
      }
        
/*     теперь при нажатии на заголовок нужно к этому заголовку добавить/удалить класс active,и с помощью next обращаемся к следующему элементу после заголовка(это текст) и анимируем его появление*/        $(this).toggleClass('active').next().slideToggle(300);
    });
    
});

/*озвучивание текста при нажатии на логотип */
function speak(text){
const message = new SpeechSynthesisUtterance();
message.lang = "ru-RU";
message.text = text;
window.speechSynthesis.speak(message);
}

const btnSpeak = document.querySelector("#speak");
const txtMessage = document.querySelector("#message");

btnSpeak.addEventListener("click",() => {
speak(txtMessage.value);
});
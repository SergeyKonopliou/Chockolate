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

/*изменение картинок в галерее(пробная версия)*/
let count = 0;
let masImage = ["images/images_gallery_1.jpg", "images/images_gallery_2.jpg", "images/images_gallery_3.jpg"];
let masNameImage = ["Picture example #1","Picture example #2","Picture example #3"];

function changeImage() {
    if (count >= masImage.length) count = 0;
    else if (count == -1) count = 2;
    document.getElementsByClassName('gallery_images').innerHTML = `<img src=${masImage[count]} alt="" class="gallery_images">`;
    document.getElementsByClassName('sign_gallery_picture')[0].innerHTML = `${masNameImage[count]}`;
}
/*можно реализовать повторное выполнение через setInterval или через рекурсивный setTimeout(создаем левую функцию например tick,внутри её вызываем или выполняем что нам нужно и setTimeout(tick, 5000) - рекурсивно опять вызываем её)*/
//let timerId = setInterval(() => changeImage(count++), 5000);
let timerId = setTimeout(function tick() {
    changeImage(count++);
    setTimeout(tick, 3000);
}, 3000);
right.onclick = () => changeImage(count++);
left.onclick = () => changeImage(count--);
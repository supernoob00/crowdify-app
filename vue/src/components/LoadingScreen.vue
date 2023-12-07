<template>
  <div class="loading">
    <img src="../assets/ms_paint_loading_screen.gif" width="250">
  </div>
</template>

<script>
export default {
  data() {
    return {
      image_urls: {
        ms_paint: 'ms_paint_loading_screen.gif',
        ping_pong: 'ping_pong_loader.gif',
        green_money_flip: 'green-money-flip.gif',
        rainbow_balls: 'rainbow_balls_loader.gif',
        basic: 'basic_loader.gif'
      }
    }
  }
}
</script>

<style scoped>
.loading {
  position: absolute;
  right: 0;
}
</style>
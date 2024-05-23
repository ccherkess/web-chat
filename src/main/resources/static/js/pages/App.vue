<template>
    <router-view :frontend-data="frontendData"></router-view>
</template>

<script>
    export default {
        data() {
          return {
            frontendData: window.frontendData || { isAdmin: false }
          };
        }
    };
</script>

<style>

</style>
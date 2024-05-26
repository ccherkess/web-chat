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
    * {
       font-family: Futura,Trebuchet MS,Arial,sans-serif;
    }
</style>
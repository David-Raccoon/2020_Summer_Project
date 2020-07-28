<template>
<div>
    <nav class="navbar navbar-expand-sm bg-dark navbar-dark display">
        <!-- Brand/logo -->
        <p class="navbar-brand">
            Contact Us: 18307130214@fudan.edu.cn<br> Copyright &copy; 2020 Web fundamental. All Rights Reserved.
        </p>
    </nav>
    <nav class="navbar navbar-expand-sm bg-dark navbar-dark hidden">
        <!-- Brand/logo -->
        <p class="navbar-brand">
            Contact Us: 18307130214@fudan.edu.cn<br> Copyright &copy; 2020 Web fundamental. All Rights Reserved.
        </p>
    </nav>
</div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min.js'

export default {
    data() {
        return {}
    }
}
</script>

<style scoped>
.hidden {
    visibility: hidden
}

.display {
    position: fixed;
    bottom: 0;
    width: 100%
}

.navbar-brand {
    width: 100%;
    text-align: center;
}
</style>

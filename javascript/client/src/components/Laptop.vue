<template>
  <div id="app">
    <!-- Navigation bar -->
    <nav class="navbar container-fluid sticky-top navbar-light bg-light">
      <form class="form-inline">
        <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
        <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
      </form>
    </nav>
    <br />
    <!-- Card container -->
    <div class="container-fluid">
    </div>
  </div>
</template>

<script>
  import axios from 'axios';
  export default {
    data () {
      return{
        laptopObj: null
      }
    },
    methods: {
      getLaptop() {
        axios
        .get('http://localhost:3000/laptops/'+this.$route.params.id)
        .then(response => this.laptopObj = response.data)
        .catch(error => {
          console.log(error)
          this.errored = true
        })
        .finally(() => this.loading = false)
      }
    },
    created() {
      this.getLaptop();
    },
  }
</script>

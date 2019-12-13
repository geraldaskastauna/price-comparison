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
      <a href="" class="text-decoration-none">
        <div class="row justify-content-center">
          <div v-bind:key="laptop.id" v-for="laptop in displayedLaptops" class="card d-flex justify-content-center rgba-black-strong py-3 px-4 rounded" style="max-width: 24rem;" href="#">
            <img :src="laptop.image_url" class="card-img-top" height="400px" width="200px" alt="Laptop picture">
            <div class="card-body col d-flex flex-column text-dark bg-light col-md">
              <h4 class="card-title text-uppercase text-center">{{laptop.brand}}</h4>
              <p class="card-text col justify-content-center">{{laptop.description}}</p>
              <div class="row">
                <div class="col-auto">
                  <p class="card-text"><b>£{{laptop.price}}</b></p>
                </div>
                <div class="col">
                  <a :href="laptop.domain + laptop.query_string" target="_blank" class="btn btn-primary align-bottom">Visit product website</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </a>
    </div>
    <br />

    <div class="row container-fluid justify-content-md-center">
      <div class="col-auto">
        <button type="button" class="btn btn-outline-primary" v-if="page != 1" @click="page--">Previous</button>
        <button type="button" class="btn btn-outline-primary" v-else>Previous</button>
        <button type="button" class="btn btn-outline-primary" v-for="pageNumber in pages.slice(page-1, page+4)" @click="page = pageNumber" v-bind:key="pageNumber"> {{pageNumber}} </button>
        <button type="button" class="btn btn-outline-primary" @click="page++" v-if="page < pages.length">Next</button>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from 'axios';
  export default {
    // name: 'app',
    data () {
      return{
        laptops: [],
        page: 1,
        items_per_page: 12,
        pages: []
      }
    },
    methods:{
      getLaptop(){
        axios
        .get('http://localhost:3000/laptops')
        .then(response => this.laptops = response.data)
        .catch(error => {
          console.log(error)
          this.errored = true
        })
      },
      setPages() {
        let numberOfPages = Math.ceil(this.laptops.length / this.items_per_page);
        for (let index = 1; index <= numberOfPages; index++) {
          this.pages.push(index);
        }
      },
      paginate (laptops) {
        let page = this.page;
        let perPage = this.items_per_page;
        let from = (page * perPage) - perPage;
        let to = (page * perPage);
        return  laptops.slice(from, to);
      }
    },

    created() {
      this.getLaptop();
    },
    mounted () {

    },
    watch: {
      laptops() {
        this.setPages();
      }
    },
    computed: {
      displayedLaptops() {
        return this.paginate(this.laptops);
      }
    }
  }
</script>

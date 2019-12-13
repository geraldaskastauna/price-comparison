import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import Laptop from '../components/Laptop.vue'
export default new VueRouter({
  mode: 'history',
  routes: [
    { path: '/laptops/:id', name: 'laptop', component: Laptop},
  ]
});

import Vue from 'vue'
import App from './App.vue'
import store from './store'
import Axios from 'axios'

Vue.config.productionTip = false

//axios全局配置
Vue.prototype.$axios = Axios
Axios.defaults.baseURL = '/api'
Axios.defaults.headers.post['Content-Type'] = 'application/json';

new Vue({
  store,
  render: h => h(App)
}).$mount('#app')

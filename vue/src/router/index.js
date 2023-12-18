import { createRouter as createRouter, createWebHashHistory } from 'vue-router'
import { useStore } from 'vuex'

import HomeView from '../views/HomeView.vue';
import LoginView from '../views/LoginView.vue';
import LogoutView from '../views/LogoutView.vue';
import RegisterView from '../views/RegisterView.vue';
import CampaignView from '../views/CampaignView.vue';
import CreateCampaignView from '../views/CreateCampaignView.vue';
import EditCampaignView from '../views/EditCampaignView.vue';
import CreateDonationView from '../views/CreateDonationView.vue';
import MyAccountView from '../views/MyAccountView.vue';
import SpendRequestView from '../views/SpendRequestView.vue';
import CreateSpendRequestView from '../views/CreateSpendRequestView.vue'
import EditSpendRequestView from '../views/EditSpendRequestView.vue';
import ForbiddenView from '../views/ForbiddenView.vue';
/*
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/logout",
    name: "logout",
    component: LogoutView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/register",
    name: "register",
    component: RegisterView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/campaigns/:id",
    name: "CampaignView",
    component: CampaignView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/campaigns/create",
    name: 'CreateCampaignView',
    component: CreateCampaignView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/campaigns/:id/edit",
    name: 'EditCampaignView',
    component: EditCampaignView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/campaigns/:id/donate",
    name: 'CreateDonationView',
    component: CreateDonationView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/campaigns/:id/spend-requests/create",
    name: 'CreateSpendRequestView',
    component: CreateSpendRequestView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/campaigns/:campaignId/spend-requests/:spendRequestId",
    name: "SpendRequestView",
    component: SpendRequestView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/campaigns/:campaignId/spend-requests/:spendRequestId/edit",
    name: 'EditSpendRequestView',
    component: EditSpendRequestView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/users/:id",
    name: 'MyAccountView',
    component: MyAccountView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/forbidden',
    name: 'forbidden',
    component: ForbiddenView
  }
];

// Create the router
const router = createRouter({
  history: createWebHashHistory(),
  routes: routes
});

router.beforeEach((to) => {

  // Get the Vuex store
  const store = useStore();

  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    return { name: "login" };
  }
  
  // Otherwise, do nothing and they'll go to their next destination
});

export default router;

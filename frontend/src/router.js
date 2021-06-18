
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import BiddingManagementManager from "./components/BiddingManagementManager"

import BiddingParticipationManager from "./components/BiddingParticipationManager"

import BiddingExaminationManager from "./components/BiddingExaminationManager"


import BiddingStatusInquiry from "./components/BiddingStatusInquiry"
import SmsHistoryManager from "./components/SMSHistoryManager"

export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/BiddingManagement',
                name: 'BiddingManagementManager',
                component: BiddingManagementManager
            },

            {
                path: '/BiddingParticipation',
                name: 'BiddingParticipationManager',
                component: BiddingParticipationManager
            },

            {
                path: '/BiddingExamination',
                name: 'BiddingExaminationManager',
                component: BiddingExaminationManager
            },


            {
                path: '/BiddingStatusInquiry',
                name: 'BiddingStatusInquiry',
                component: BiddingStatusInquiry
            },
            {
                path: '/SMSHistory',
                name: 'SMSHistoryManager',
                component: SMSHistoryManager
            },



    ]
})

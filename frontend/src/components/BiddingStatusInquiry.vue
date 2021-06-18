<template>

<v-data-table
    :headers="headers"
    :items="biddingStatusInquiry"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'BiddingStatusInquiry',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "id", value: "id" },
            { text: "입찰공고번호", value: "입찰공고번호" },
            { text: "입찰명", value: "입찰명" },
            { text: "companyNm", value: "companyNm" },
            { text: "biddingResult", value: "biddingResult" },
        ],
        biddingStatusInquiry : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/biddingstatusinquiries')

      this.biddingStatusInquiry = temp.data._embedded.biddingstatusinquiries;

    },
    methods: {
    }
  }
</script>


require.config({
  baseUrl: './',
  paths: {
    jquery: 'bower_components/jquery/jquery',
    streetview: 'src/streetview',
    google: 'src/googlemaps',
    'googlemaps-loader' : 'src/googlemaps-loader',
    'i18n': 'bower_components/requirejs-i18n/i18n',
    'nls': 'nls/'
  },
  waitSeconds: 0,
  config: {
    i18n: {
      locale: "es" //Unicode language and region identifier accepted
    },
    streetview: {
      origin: {
        lat: 42.059338 ,    
        lng: -1.59798
      }
    },
    'googlemaps-loader': {
      language: "es", //Unicode language and region identifier accepted
      region: "es",
      googleClientID: "",
      googleChannelID: "Aguas_Tudela_live"
    }
  }
});

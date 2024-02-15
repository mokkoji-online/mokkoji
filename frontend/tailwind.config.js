/** @type {import('tailwindcss').Config} */

export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    container: {
      center: true
    },

    extend: {
      colors: {
        'primary': '#C8B6FF',
        'primary2': '#E1F4F8',
        'primary3': '#AB90FF',
        'primary0': '#F9F6FF',
        'white': '#FFFFFF',
        'black': '#000000',
        'gray': '#EFEFEF',
        'natural-beige': '#FCF7FF',
        'natural-yellow': '#fffade',
        'gradient-blue' : '#80AEF8',
        'spot-purple' : '#9183C6',
      }
    }

  },
  plugins: [
    // eslint-disable-next-line no-undef
    require("tailwindcss-animate"),

  ],

}

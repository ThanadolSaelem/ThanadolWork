const searchresult = document.querySelector(".result");
const searchInput = document.querySelector(".search-input");
const searchButton = document.querySelector(".search-button");
const regionBtns = document.querySelector(".regionbtn");
const clearBtn = document.querySelector(".clear_btn");

const getWeather = function (country) {
  const req = new XMLHttpRequest();
  req.open(
    "GET",
    `https://api.openweathermap.org/data/2.5/weather?q=${country}&appid=bdcdf19d695a088cee97966de5d8ca23`
  );
  req.send();

  req.addEventListener("load", function () {
    const data = JSON.parse(this.responseText);
    const weather = data.weather[0].description;

    const html = `
    <p class="country_row"><span>⛅</span>${weather}</p>
    `;
    const countryElement = document.querySelector(`.country[data-name="${country}"]`);
    countryElement.querySelector(".country_weather").insertAdjacentHTML("beforeend", html);
  });
};

const getCountry = function (country) {
  const req = new XMLHttpRequest();
  req.open("GET", `https://restcountries.com/v3.1/name/${country}`);
  req.send();

  
  req.addEventListener("load", function () {
    const [data] = JSON.parse(this.responseText);
    const lang = Object.entries(data.languages);
    const curr = Object.values(data.currencies).map(
      (currency) => `${currency.name} (${currency.symbol})`
    );

    const html = `
      <article class="country" data-name="${data.name.common}">
        <img src="${data.flags.png}" class="country_img" />
        <div class="country_data">
          <h3 class="country_name">${data.name.common}</h3>
          <h4 class="country_region">${data.region}</h4>
          <p class="country_row"><span>🙋</span>${data.population}</p>
          <p class="country_row"><span>💬</span>${lang[0][1]}</p>
          <p class="country_row"><span>💰</span>${curr}</p>
          <div class="country_weather"></div>
        </div>
      </article>
    `;
    searchresult.insertAdjacentHTML("beforeend", html);
    getWeather(data.name.common);
  });
};

searchButton.addEventListener("click", function () {
  const countryName = searchInput.value.trim();
  if (countryName) {
    searchresult.innerHTML = "";
    getCountry(countryName);
  }
});

clearBtn.addEventListener("click", function () {
  searchresult.innerHTML = "";
});

const getRegions = function () {
  const req = new XMLHttpRequest();
  req.open("GET", "https://restcountries.com/v3.1/all");
  req.send();

  req.addEventListener("load", function () {
    const data = JSON.parse(req.responseText);
    const regionsSet = new Set();

    data.forEach((country) => regionsSet.add(country.region));

    regionsSet.forEach((regiondata) => {
      const btn = document.createElement("button");
      btn.classList.add("region_btn");
      btn.textContent = regiondata;
      regionBtns.appendChild(btn);
      btn.addEventListener("click", function () {
        searchresult.innerHTML = "";
        data
          .filter((country) => country.region === regiondata)
          .forEach((country) => getCountry(country.name.common));
      });
    });
  });
};
getRegions();

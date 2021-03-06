package com.etiya.ReCapProject.business.abstracts;

import java.util.List;

import com.etiya.ReCapProject.core.utilities.results.DataResult;
import com.etiya.ReCapProject.core.utilities.results.Result;
import com.etiya.ReCapProject.entities.concretes.City;
import com.etiya.ReCapProject.entities.dtos.CityDetailDto;
import com.etiya.ReCapProject.entities.requests.create.CreateCityRequest;
import com.etiya.ReCapProject.entities.requests.delete.DeleteCityRequest;
import com.etiya.ReCapProject.entities.requests.update.UpdateCityRequest;

public interface CityService {
	DataResult<List<City>> getAll();

	DataResult<City> getById(int cityId);

	DataResult<List<CityDetailDto>> getCitysDetail();

	DataResult<CityDetailDto> getCityDetailById(int cityId);

	Result add(CreateCityRequest createCityRequest);

	Result update(UpdateCityRequest updateCityRequest);

	Result delete(DeleteCityRequest deleteCityRequest);
}

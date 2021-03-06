package com.etiya.ReCapProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etiya.ReCapProject.business.abstracts.ColorService;
import com.etiya.ReCapProject.business.abstracts.ModelMapperService;
import com.etiya.ReCapProject.business.constants.Messages;
import com.etiya.ReCapProject.core.utilities.business.BusinessRules;
import com.etiya.ReCapProject.core.utilities.results.DataResult;
import com.etiya.ReCapProject.core.utilities.results.ErrorResult;
import com.etiya.ReCapProject.core.utilities.results.Result;
import com.etiya.ReCapProject.core.utilities.results.SuccessDataResult;
import com.etiya.ReCapProject.core.utilities.results.SuccessResult;
import com.etiya.ReCapProject.dataAccess.abstracts.ColorDao;
import com.etiya.ReCapProject.entities.concretes.Color;
import com.etiya.ReCapProject.entities.dtos.ColorDetailDto;
import com.etiya.ReCapProject.entities.requests.create.CreateColorRequest;
import com.etiya.ReCapProject.entities.requests.delete.DeleteColorRequest;
import com.etiya.ReCapProject.entities.requests.update.UpdateColorRequest;

@Service
public class ColorManager implements ColorService {

	private ColorDao colorDao;
	private ModelMapperService modelMapperService;

	@Autowired
	public ColorManager(ColorDao colorDao, ModelMapperService modelMapperService) {
		super();
		this.colorDao = colorDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<Color>> getAll() {

		return new SuccessDataResult<List<Color>>(this.colorDao.findAll(), Messages.ColorsListed);
	}

	@Override
	public DataResult<Color> getById(int colorId) {

		return new SuccessDataResult<Color>(this.colorDao.getById(colorId), Messages.ColorListed);
	}

	@Override
	public DataResult<List<ColorDetailDto>> getColorsDetail() {

		List<Color> colors = this.colorDao.findAll();

		List<ColorDetailDto> colorDetailDtos = colors.stream()
				.map(color -> modelMapperService.forDto().map(color, ColorDetailDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ColorDetailDto>>(colorDetailDtos, Messages.ColorsListed);
	}

	@Override
	public DataResult<ColorDetailDto> getColorDetailById(int colorId) {

		Color color = this.colorDao.getById(colorId);

		return new SuccessDataResult<ColorDetailDto>(modelMapperService.forDto().map(color, ColorDetailDto.class),
				Messages.ColorListed);
	}

	@Override
	public Result add(CreateColorRequest createColorRequest) {

		var result = BusinessRules.run(this.checkColorByColorName(createColorRequest.getColorName()));

		if (result != null) {
			return result;
		}

		Color color = modelMapperService.forRequest().map(createColorRequest, Color.class);

		this.colorDao.save(color);
		return new SuccessResult(Messages.ColorAdded);

	}

	@Override
	public Result update(UpdateColorRequest updateColorRequest) {

		var result = BusinessRules.run(this.checkColorByColorName(updateColorRequest.getColorName()));

		if (result != null) {
			return result;
		}

		Color color = this.colorDao.getById(updateColorRequest.getColorId());
		color.setColorName(updateColorRequest.getColorName());

		this.colorDao.save(color);
		return new SuccessResult(Messages.ColorUpdated);
	}

	@Override
	public Result delete(DeleteColorRequest deleteColorRequest) {

		Color color = this.colorDao.getById(deleteColorRequest.getColorId());

		this.colorDao.delete(color);
		return new SuccessResult(Messages.ColorDeleted);
	}

	private Result checkColorByColorName(String colorName) {
		if (this.colorDao.existsByColorName(colorName)) {
			return new ErrorResult(Messages.ColorIsFound);
		}
		return new SuccessResult();
	}

}

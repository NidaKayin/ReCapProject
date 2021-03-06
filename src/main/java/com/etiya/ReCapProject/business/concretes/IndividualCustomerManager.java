package com.etiya.ReCapProject.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etiya.ReCapProject.business.abstracts.IndividualCustomerService;
import com.etiya.ReCapProject.business.abstracts.ModelMapperService;
import com.etiya.ReCapProject.business.abstracts.UserService;
import com.etiya.ReCapProject.business.constants.Messages;
import com.etiya.ReCapProject.core.utilities.results.DataResult;
import com.etiya.ReCapProject.core.utilities.results.ErrorResult;
import com.etiya.ReCapProject.core.utilities.results.Result;
import com.etiya.ReCapProject.core.utilities.results.SuccessDataResult;
import com.etiya.ReCapProject.core.utilities.results.SuccessResult;
import com.etiya.ReCapProject.dataAccess.abstracts.IndividualCustomerDao;
import com.etiya.ReCapProject.entities.concretes.ApplicationUser;
import com.etiya.ReCapProject.entities.concretes.IndividualCustomer;
import com.etiya.ReCapProject.entities.dtos.IndividualCustomerDetailDto;
import com.etiya.ReCapProject.entities.requests.create.CreateIndividualCustomerRequest;
import com.etiya.ReCapProject.entities.requests.delete.DeleteIndividualCustomerRequest;
import com.etiya.ReCapProject.entities.requests.update.UpdateIndividualCustomerRequest;

@Service
public class IndividualCustomerManager implements IndividualCustomerService {

	private IndividualCustomerDao individualCustomerDao;
	private UserService userService;
	private ModelMapperService modelMapperService;

	@Autowired
	public IndividualCustomerManager(IndividualCustomerDao individualCustomerDao, UserService userService,
			ModelMapperService modelMapperService) {
		super();
		this.individualCustomerDao = individualCustomerDao;
		this.userService = userService;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<IndividualCustomer>> getAll() {

		return new SuccessDataResult<List<IndividualCustomer>>(this.individualCustomerDao.findAll(),
				Messages.CustomersListed);
	}

	@Override
	public DataResult<IndividualCustomer> getById(int individualCustomerId) {

		return new SuccessDataResult<IndividualCustomer>(this.individualCustomerDao.getById(individualCustomerId),
				Messages.CustomerListed);
	}

	@Override
	public DataResult<IndividualCustomerDetailDto> getIndividualCustomerDetailsById(int individualCustomerId) {

		IndividualCustomer individualCustomer = this.individualCustomerDao.getById(individualCustomerId);

		IndividualCustomerDetailDto individualCustomerDetailDto = modelMapperService.forDto().map(individualCustomer,
				IndividualCustomerDetailDto.class);
		individualCustomerDetailDto.setEmail(individualCustomer.getApplicationUser().getEmail());

		return new SuccessDataResult<IndividualCustomerDetailDto>(individualCustomerDetailDto,
				Messages.IndividualCustomerDetail);
	}

	@Override
	public DataResult<IndividualCustomer> getByUserId(int applicationUserId) {

		return new SuccessDataResult<IndividualCustomer>(
				this.individualCustomerDao.getByApplicationUser_UserId(applicationUserId));
	}

	@Override
	public Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) {

		ApplicationUser applicationUser = this.userService.getById(createIndividualCustomerRequest.getUserId())
				.getData();

		IndividualCustomer individualCustomer = modelMapperService.forRequest().map(createIndividualCustomerRequest,
				IndividualCustomer.class);

		individualCustomer.setApplicationUser(applicationUser);

		this.individualCustomerDao.save(individualCustomer);

		return new SuccessResult(Messages.CustomerAdded);
	}

	@Override
	public Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest) {

		IndividualCustomer individualCustomer = this.individualCustomerDao
				.getById(updateIndividualCustomerRequest.getIndividualCustomerId());

		individualCustomer.setFirstName(updateIndividualCustomerRequest.getFirstName());
		individualCustomer.setLastName(updateIndividualCustomerRequest.getLastName());
		individualCustomer.setNationalIdentityNumber(updateIndividualCustomerRequest.getNationalIdentityNumber());

		this.individualCustomerDao.save(individualCustomer);

		return new SuccessResult(Messages.CustomerUpdated);
	}

	@Override
	public Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) {

		IndividualCustomer individualCustomer = this.individualCustomerDao
				.getById(deleteIndividualCustomerRequest.getIndividualCustomerId());

		this.individualCustomerDao.delete(individualCustomer);

		return new SuccessResult(Messages.CustomerDeleted);
	}

	@Override
	public Result existsByUserId(int applicationUserId) {

		if (this.individualCustomerDao.existsByApplicationUser_UserId(applicationUserId)) {
			return new SuccessResult();
		}
		return new ErrorResult();
	}

}

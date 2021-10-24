package pl.futurecollars.invoicing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Calculation;
import pl.futurecollars.invoicing.service.TaxCalculatorService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/calculation")
@Api(tags = {"calculation-controller"})
public class TaxCalculatorController {

    private final TaxCalculatorService calculatorService;

    @GetMapping("/{taxId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get calculation for company with given tax id")
    public ResponseEntity<Calculation> getCalculation(@PathVariable String taxId) {
        try {
            return ResponseEntity.ok(calculatorService.getCalculation(taxId));
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }
}

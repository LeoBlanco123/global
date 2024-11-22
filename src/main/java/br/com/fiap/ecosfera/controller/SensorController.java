package br.com.fiap.ecosfera.controller;

import br.com.fiap.ecosfera.model.Sensor;
import br.com.fiap.ecosfera.model.Empresas;
import br.com.fiap.ecosfera.repository.SensorRepository;
import br.com.fiap.ecosfera.repository.EmpresasRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/sensores")
public class SensorController {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private EmpresasRepository empresasRepository;

    @GetMapping
    public ModelAndView listarSensores() {
        List<Sensor> listaSensores = sensorRepository.findAll();
        ModelAndView mv = new ModelAndView("index_sensor");
        mv.addObject("sensores", listaSensores);
        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novaSensorPage(@RequestParam(value = "idEmpresa", required = false) Long idEmpresa) {
        ModelAndView mv = new ModelAndView("criar_sensor");
        Sensor sensor = new Sensor();

        if (idEmpresa != null) {
            Optional<Empresas> empresaOptional = empresasRepository.findById(idEmpresa);
            if (empresaOptional.isPresent()) {
                sensor.setEmpresa(empresaOptional.get());
            } else {
                mv.setViewName("redirect:/sensor");
                return mv;
            }
        }

        mv.addObject("sensor", sensor);
        mv.addObject("empresas", empresasRepository.findAll());
        return mv;
    }

    @PostMapping("/salvar")
    public ModelAndView salvarSensor(@Valid @ModelAttribute("sensor") Sensor sensor, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("criar_sensor");
            mv.addObject("sensor", sensor);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        if (sensor.getEmpresa() == null || sensor.getEmpresa().getId() == null) {
            ModelAndView mv = new ModelAndView("criar_sensor");
            mv.addObject("errorMessage", "Por favor, selecione uma empresa.");
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        try {
            sensorRepository.save(sensor);
            Long idEmpresa = sensor.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes");
        } catch (Exception e) {
            System.out.println("Erro ao salvar o sensor: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editarSensorPage(@PathVariable Long id) {
        Optional<Sensor> sensorOptional = sensorRepository.findById(id);

        if (sensorOptional.isPresent()) {
            ModelAndView mv = new ModelAndView("editar_sensor");
            mv.addObject("sensor", sensorOptional.get());
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        } else {
            return new ModelAndView("redirect:/sensor");
        }
    }

    @PostMapping("/{id}/editar")
    public ModelAndView atualizarSensor(@PathVariable Long id, @Valid @ModelAttribute("sensor") Sensor sensor,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("editar_sensor");
            mv.addObject("sensor", sensor);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        Optional<Sensor> op = sensorRepository.findById(id);

        if (op.isPresent()) {
            Sensor s = op.get();
            s.setTipo(sensor.getTipo());
            s.setLocalizacao(sensor.getLocalizacao());
            s.setLeituraAtual(sensor.getLeituraAtual());
            s.setEmpresa(sensor.getEmpresa());

            sensorRepository.save(s);

            Long idEmpresa = s.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes");
        } else {
            return new ModelAndView("redirect:/sensor");
        }
    }

    @PostMapping("/{id}/remover")
    public String removeSensor(@PathVariable Long id) {
        try {
            Optional<Sensor> sensorOptional = sensorRepository.findById(id);
            if (sensorOptional.isPresent()) {
                Sensor sensor = sensorOptional.get();
                Long idEmpresa = sensor.getEmpresa().getId();
                sensorRepository.deleteById(id);
                return "redirect:/empresas/" + idEmpresa + "/detalhes";
            } else {
                return "redirect:/sensor";
            }
        } catch (Exception e) {
            System.out.println("Erro ao remover o sensor: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/detalhes")
    public ModelAndView exibirDetalhes(@PathVariable Long id) {
        Optional<Sensor> sensorOpt = sensorRepository.findById(id);

        if (sensorOpt.isPresent()) {
            Sensor sensor = sensorOpt.get();
            ModelAndView mv = new ModelAndView("detalhes_sensor");
            mv.addObject("sensor", sensor);
            return mv;
        } else {
            return new ModelAndView("redirect:/sensor");
        }
    }
}

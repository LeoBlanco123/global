package br.com.fiap.ecosfera.controller;

import br.com.fiap.ecosfera.model.AnaliseImpacto;
import br.com.fiap.ecosfera.model.Empresas;
import br.com.fiap.ecosfera.repository.AnaliseImpactoRepository;
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
@RequestMapping("/analise")
public class AnaliseImpactoController {

    @Autowired
    private AnaliseImpactoRepository analiseImpactoRepository;

    @Autowired
    private EmpresasRepository empresasRepository;

    @GetMapping
    public ModelAndView listarAnaliseImpacto() {
        List<AnaliseImpacto> listaAnaliseImpacto = analiseImpactoRepository.findAll();
        ModelAndView mv = new ModelAndView("index_comportamento");
        mv.addObject("analiseImpacto", listaAnaliseImpacto);
        return mv;
    }

    @GetMapping("/nova")
    public ModelAndView novaAnaliseImpactoPage(@RequestParam(value = "idEmpresa", required = false) Long idEmpresa) {
        ModelAndView mv = new ModelAndView("criar_analise");
        AnaliseImpacto analiseImpacto = new AnaliseImpacto();

        if (idEmpresa != null) {
            Optional<Empresas> empresaOptional = empresasRepository.findById(idEmpresa);
            if (empresaOptional.isPresent()) {
                analiseImpacto.setEmpresa(empresaOptional.get());
            } else {
                mv.setViewName("redirect:/analise");
                return mv;
            }
        }

        mv.addObject("analiseImpacto", analiseImpacto);
        mv.addObject("empresas", empresasRepository.findAll());
        return mv;
    }


    @PostMapping("/salvar")
    public ModelAndView salvarAnaliseImpacto(@Valid @ModelAttribute("analiseImpacto") AnaliseImpacto analiseImpacto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("criar_analise");
            mv.addObject("analiseImpacto", analiseImpacto);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        if (analiseImpacto.getEmpresa() == null || analiseImpacto.getEmpresa().getId() == null) {
            ModelAndView mv = new ModelAndView("criar_analise");
            mv.addObject("errorMessage", "Por favor, selecione uma empresa.");
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        try {
            analiseImpactoRepository.save(analiseImpacto);
            Long idEmpresa = analiseImpacto.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes");
        } catch (Exception e) {
            System.out.println("Erro ao salvar a análise de impacto: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editarAnalisePage(@PathVariable Long id) {
        Optional<AnaliseImpacto> analiseOptional = analiseImpactoRepository.findById(id);

        if (analiseOptional.isPresent()) {
            ModelAndView mv = new ModelAndView("editar_analise");
            mv.addObject("analiseImpacto", analiseOptional.get());
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        } else {
            return new ModelAndView("redirect:/analise");
        }
    }

    @PostMapping("/{id}/editar")
    public ModelAndView atualizarAnalise(@PathVariable Long id, @Valid @ModelAttribute("analise") AnaliseImpacto analiseImpacto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("editar_analise");
            mv.addObject("analiseImpacto", analiseImpacto);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        Optional<AnaliseImpacto> op = analiseImpactoRepository.findById(id);

        if (op.isPresent()) {
            AnaliseImpacto a = op.get();
            a.setCumprimentoMetas(analiseImpacto.getCumprimentoMetas());
            a.setRecomendacoes(analiseImpacto.getRecomendacoes());
            a.setProgressoPegadaCarbono(analiseImpacto.getProgressoPegadaCarbono());
            a.setEmpresa(analiseImpacto.getEmpresa());

            analiseImpactoRepository.save(a);

            Long idEmpresa = a.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes");
        } else {
            return new ModelAndView("redirect:/analise");
        }

    }

    @PostMapping("/{id}/remover")
    public String removeAnalise(@PathVariable Long id) {
        try {
            Optional<AnaliseImpacto> analiseOptional = analiseImpactoRepository.findById(id);
            if (analiseOptional.isPresent()) {
                AnaliseImpacto analise = analiseOptional.get();
                Long idEmpresa = analise.getEmpresa().getId();
                analiseImpactoRepository.deleteById(id);
                return "redirect:/empresas/" + idEmpresa + "/detalhes";

            } else {
                return "redirect:/analise";
            }
        } catch (Exception e) {
            System.out.println("Erro ao remover a análise de impacto: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/detalhes")
    public ModelAndView exibirDetalhes(@PathVariable Long id) {
        Optional<Empresas> empresaOpt = empresasRepository.findById(id);

        if (empresaOpt.isPresent()) {
            Empresas empresa = empresaOpt.get();
            List<AnaliseImpacto> analise = analiseImpactoRepository.findByEmpresaId(id);

            ModelAndView mv = new ModelAndView("detalhes_empresa");
            mv.addObject("empresa", empresa);
            mv.addObject("analise", analise);
            return mv;
        } else {
            return new ModelAndView("redirect:/empresas");
        }
    }
}
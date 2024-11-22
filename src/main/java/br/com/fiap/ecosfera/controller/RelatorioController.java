package br.com.fiap.ecosfera.controller;

import br.com.fiap.ecosfera.model.AnaliseImpacto;
import br.com.fiap.ecosfera.model.Relatorio;
import br.com.fiap.ecosfera.model.Empresas;
import br.com.fiap.ecosfera.repository.RelatorioRepository;
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
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private EmpresasRepository empresasRepository;

    @GetMapping
    public ModelAndView listarRelatorios() {
        List<Relatorio> listaRelatorios = relatorioRepository.findAll();
        ModelAndView mv = new ModelAndView("index_relatorio");
        mv.addObject("relatorios", listaRelatorios);
        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novoRelatorioPage(@RequestParam(value = "idEmpresa", required = false) Long idEmpresa) {
        ModelAndView mv = new ModelAndView("criar_relatorio");
        Relatorio relatorio = new Relatorio();

        if (idEmpresa != null) {
            Optional<Empresas> empresaOptional = empresasRepository.findById(idEmpresa);
            if (empresaOptional.isPresent()) {
                relatorio.setEmpresa(empresaOptional.get());
            } else {
                mv.setViewName("redirect:/relatorios");
                return mv;
            }
        }

        mv.addObject("relatorio", relatorio);
        mv.addObject("empresas", empresasRepository.findAll());
        return mv;
    }

    @PostMapping("/salvar")
    public ModelAndView salvarRelatorio(@Valid @ModelAttribute("relatorio") Relatorio relatorio, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("criar_relatorio");
            mv.addObject("relatorio", relatorio);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        if (relatorio.getEmpresa() == null || relatorio.getEmpresa().getId() == null) {
            ModelAndView mv = new ModelAndView("criar_relatorio");
            mv.addObject("errorMessage", "Por favor, selecione uma empresa.");
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        try {
            relatorioRepository.save(relatorio);
            Long idEmpresa = relatorio.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes");
        } catch (Exception e) {
            System.out.println("Erro ao salvar o relatório: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editarRelatorioPage(@PathVariable Long id) {
        Optional<Relatorio> relatorioOptional = relatorioRepository.findById(id);

        if (relatorioOptional.isPresent()) {
            ModelAndView mv = new ModelAndView("editar_relatorio");
            mv.addObject("relatorio", relatorioOptional.get());
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        } else {
            return new ModelAndView("redirect:/relatorios");
        }
    }

    @PostMapping("/{id}/editar")
    public ModelAndView atualizarRelatorio(@PathVariable Long id, @Valid @ModelAttribute("relatorio") Relatorio relatorio,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("editar_relatorio");
            mv.addObject("relatorio", relatorio);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        Optional<Relatorio> op = relatorioRepository.findById(id);

        if (op.isPresent()) {
            Relatorio r = op.get();
            r.setEmissaoGases(relatorio.getEmissaoGases());
            r.setUsoAgua(relatorio.getUsoAgua());
            r.setEnergiaConsumida(relatorio.getEnergiaConsumida());
            r.setEmpresa(relatorio.getEmpresa());

            relatorioRepository.save(r);

            Long idEmpresa = r.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes");
        } else {
            return new ModelAndView("redirect:/relatorios");
        }
    }

    @PostMapping("/{id}/remover")
    public String removerRelatorio(@PathVariable Long id) {
        try {
            Optional<Relatorio> relatorioOptional = relatorioRepository.findById(id);
            if (relatorioOptional.isPresent()) {
                Relatorio relatorio = relatorioOptional.get();
                Long idEmpresa = relatorio.getEmpresa().getId();
                relatorioRepository.deleteById(id);
                return "redirect:/empresas/" + idEmpresa + "/detalhes";
            } else {
                return "redirect:/relatorios";
            }
        } catch (Exception e) {
            System.out.println("Erro ao remover o relatório: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/detalhes")
    public ModelAndView exibirDetalhes(@PathVariable Long id) {
        Optional<Empresas> empresaOpt = empresasRepository.findById(id);

        if (empresaOpt.isPresent()) {
            Empresas empresa = empresaOpt.get();
            List<Relatorio> relatorio = relatorioRepository.findByEmpresaId(id);

            ModelAndView mv = new ModelAndView("detalhes_empresa");
            mv.addObject("empresa", empresa);
            mv.addObject("relatorio", relatorio);
            return mv;
        } else {
            return new ModelAndView("redirect:/empresas");
        }
    }
}

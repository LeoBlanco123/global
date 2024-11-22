package br.com.fiap.ecosfera.controller;

import br.com.fiap.ecosfera.model.MetaAmbiental;
import br.com.fiap.ecosfera.model.Empresas;
import br.com.fiap.ecosfera.repository.MetaAmbientalRepository;
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
@RequestMapping("/metas")
public class MetaAmbientalController {

    @Autowired
    private MetaAmbientalRepository metaAmbientalRepository;

    @Autowired
    private EmpresasRepository empresasRepository;

    @GetMapping
    public ModelAndView listarMetas() {
        List<MetaAmbiental> listaMetas = metaAmbientalRepository.findAll();
        ModelAndView mv = new ModelAndView("index_metas");
        mv.addObject("metas", listaMetas);
        return mv;
    }

    @GetMapping("/nova")
    public ModelAndView novaMetaPage(@RequestParam(value = "idEmpresa", required = false) Long idEmpresa) {
        ModelAndView mv = new ModelAndView("criar_meta");
        MetaAmbiental metaAmbiental = new MetaAmbiental();

        if (idEmpresa != null) {
            Optional<Empresas> empresaOptional = empresasRepository.findById(idEmpresa);
            if (empresaOptional.isPresent()) {
                metaAmbiental.setEmpresa(empresaOptional.get());
            } else {
                mv.setViewName("redirect:/metas");
                return mv;
            }
        }

        mv.addObject("metaAmbiental", metaAmbiental);
        mv.addObject("empresas", empresasRepository.findAll());
        return mv;
    }

    @PostMapping("/salvar")
    public ModelAndView salvarMeta(@Valid @ModelAttribute("metaAmbiental") MetaAmbiental metaAmbiental,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("criar_meta");
            mv.addObject("metaAmbiental", metaAmbiental);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        if (metaAmbiental.getEmpresa() == null || metaAmbiental.getEmpresa().getId() == null) {
            ModelAndView mv = new ModelAndView("criar_meta");
            mv.addObject("errorMessage", "Por favor, selecione uma empresa.");
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        try {
            metaAmbientalRepository.save(metaAmbiental);
            Long idEmpresa = metaAmbiental.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes");
        } catch (Exception e) {
            System.out.println("Erro ao salvar a meta ambiental: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editarMetaPage(@PathVariable Long id) {
        Optional<MetaAmbiental> metaOptional = metaAmbientalRepository.findById(id);

        if (metaOptional.isPresent()) {
            ModelAndView mv = new ModelAndView("editar_meta");
            mv.addObject("metaAmbiental", metaOptional.get());
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        } else {
            return new ModelAndView("redirect:/metas");
        }
    }

    @PostMapping("/{id}/editar")
    public ModelAndView atualizarMeta(@PathVariable Long id,
                                      @Valid @ModelAttribute("metaAmbiental") MetaAmbiental metaAmbiental,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("editar_meta");
            mv.addObject("metaAmbiental", metaAmbiental);
            mv.addObject("empresas", empresasRepository.findAll());
            return mv;
        }

        Optional<MetaAmbiental> op = metaAmbientalRepository.findById(id);

        if (op.isPresent()) {
            MetaAmbiental meta = op.get();
            meta.setDescricao(metaAmbiental.getDescricao());
            meta.setReducaoEmissao(metaAmbiental.getReducaoEmissao());
            meta.setReducaoAgua(metaAmbiental.getReducaoAgua());
            meta.setReducaoEnergia(metaAmbiental.getReducaoEnergia());
            meta.setEmpresa(metaAmbiental.getEmpresa());

            metaAmbientalRepository.save(meta);

            Long idEmpresa = meta.getEmpresa().getId();
            return new ModelAndView("redirect:/empresas/" + idEmpresa + "/detalhes");
        } else {
            return new ModelAndView("redirect:/metas");
        }
    }

    @PostMapping("/{id}/remover")
    public String removerMeta(@PathVariable Long id) {
        try {
            Optional<MetaAmbiental> metaOptional = metaAmbientalRepository.findById(id);
            if (metaOptional.isPresent()) {
                MetaAmbiental meta = metaOptional.get();
                Long idEmpresa = meta.getEmpresa().getId();
                metaAmbientalRepository.deleteById(id);
                return "redirect:/empresas/" + idEmpresa + "/detalhes";
            } else {
                return "redirect:/metas";
            }
        } catch (Exception e) {
            System.out.println("Erro ao remover a meta ambiental: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/detalhes")
    public ModelAndView exibirDetalhes(@PathVariable Long id) {
        Optional<Empresas> empresaOpt = empresasRepository.findById(id);

        if (empresaOpt.isPresent()) {
            Empresas empresa = empresaOpt.get();
            List<MetaAmbiental> metas = metaAmbientalRepository.findByEmpresaId(id);

            ModelAndView mv = new ModelAndView("detalhes_empresa");
            mv.addObject("empresa", empresa);
            mv.addObject("metas", metas);
            return mv;
        } else {
            return new ModelAndView("redirect:/empresas");
        }
    }
}

package br.com.fiap.ecosfera.controller;

import br.com.fiap.ecosfera.model.*;
import br.com.fiap.ecosfera.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/empresas")
public class EmpresasController {

    @Autowired
    private EmpresasRepository empresasRepository;

    @Autowired
    private MetaAmbientalRepository metaAmbientalRepository;

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private AnaliseImpactoRepository analiseImpactoRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ModelAndView listarEmpresas() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        List<Empresas> listaEmpresas = empresasRepository.findAll();

        ModelAndView mv = new ModelAndView("index");
        mv.addObject("empresas", listaEmpresas);

        Optional<Usuario> user = usuarioRepository.findByUsername(username);

        user.ifPresent(usuario -> mv.addObject("nome_usuario", usuario.getNome()));

        return mv;
    }


    @GetMapping("/nova")
    public ModelAndView novaEmpresaPage() {
        ModelAndView mv = new ModelAndView("criar_empresa");
        mv.addObject("empresa", new Empresas());
        return mv;
    }

    @PostMapping("/salvar")
    public ModelAndView salvarEmpresa(@Valid @ModelAttribute("empresa") Empresas empresa, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("criar_empresa");
            mv.addObject("empresa", empresa);
            return mv;
        } else {
            empresasRepository.save(empresa);
            return new ModelAndView("redirect:/empresas");
        }
    }

    @GetMapping("/{id}/editar")
    public ModelAndView retornaPaginaEdicao(@PathVariable Long id) {
        Optional<Empresas> op = empresasRepository.findById(id);

        if (op.isPresent()) {
            ModelAndView mv = new ModelAndView("editar_empresa");
            mv.addObject("empresa", op.get());
            return mv;
        } else {
            return new ModelAndView("redirect:/empresas");
        }
    }


    @PostMapping("/{id}/editar")
    public ModelAndView atualizarEmpresa(@PathVariable Long id, @Valid @ModelAttribute("empresa") Empresas empresa,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("editar_empresa");
            mv.addObject("empresa", empresa);
            return mv;
        }

        else {
            Optional<Empresas> op = empresasRepository.findById(id);

            if (op.isPresent()) {
                Empresas emp = op.get();
                emp.setNome(empresa.getNome());
                emp.setSetor(empresa.getSetor());
                emp.setLocalizacaoGeografica(empresa.getLocalizacaoGeografica());
                emp.setCarbonoAtual(empresa.getCarbonoAtual());
                empresasRepository.save(emp);
                return new ModelAndView("redirect:/empresas");
            } else {
                return new ModelAndView("redirect:/empresas");
            }
        }
    }

    @PostMapping("/{id}/remover")
    public String removeEmpresa(@PathVariable Long id) {
        empresasRepository.deleteById(id);
        return "redirect:/empresas";
    }

    @GetMapping("/{id}/detalhes")
    public ModelAndView exibirDetalhes(@PathVariable Long id) {

        Optional<Empresas> op = empresasRepository.findById(id);

        if (op.isPresent()) {
            Empresas empresa = op.get();
            List<MetaAmbiental> metas = metaAmbientalRepository.findByEmpresaId(id);
            List<Relatorio> relatorios = relatorioRepository.findByEmpresaId(id);
            List<AnaliseImpacto> analise = analiseImpactoRepository.findByEmpresaId(id);
            List<Sensor> sensores = sensorRepository.findByEmpresaId(id);

            ModelAndView mv = new ModelAndView("detalhes_empresa");
            mv.addObject("empresa", empresa);
            mv.addObject("metas", metas);
            mv.addObject("relatorios", relatorios);
            mv.addObject("Analise", analise);
            mv.addObject("sensores", sensores);

            boolean showCreateButtonMetas = metas.isEmpty();
            mv.addObject("showCreateButtonMetas", showCreateButtonMetas);

            boolean showCreateButtonRelatorios = relatorios.isEmpty();
            mv.addObject("showCreateButtonRelatorios", showCreateButtonRelatorios);

            boolean showCreateButtonAnalise = analise.isEmpty();
            mv.addObject("showCreateButtonAnalise", showCreateButtonAnalise);

            boolean showCreateButtonSensores = sensores.isEmpty();
            mv.addObject("showCreateButtonSensores", showCreateButtonSensores);
            return mv;

        } else {
            return new ModelAndView("redirect:/empresas");
        }
    }
}

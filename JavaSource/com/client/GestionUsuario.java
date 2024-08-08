package com.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.util.LangUtils;

import com.dao.CarreraEspecialidadDAO;
import com.dao.EstudianteDAO;
import com.dao.ItrDAO;
import com.dao.UsuarioDAO;
import com.dto.EstudianteDTO;
import com.dto.UsuarioDTO;
import com.entities.CarreraEspecialidad;
import com.entities.Estudiante;
import com.entities.Itr;
import com.entities.Usuario;
import com.enums.EstadoCarreraEspecialidad;
import com.enums.EstadoItr;
import com.enums.EstadoUsuario;
import com.enums.TipoUsuario;
import com.enums.Verificacion;
import com.services.AnalistaService;
import com.services.EstudianteService;
import com.utils.ValidationError;

import lombok.Data;

import java.io.Serializable;

//Esto es un NamedBean
@Named(value = "gestionUsuario")
@SessionScoped
@Data
public class GestionUsuario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private UsuarioDAO usuarioDAO;

	@EJB
	private EstudianteDAO estudianteDAO;

	@EJB
	private ItrDAO itrDAO;

	@EJB
	private CarreraEspecialidadDAO carreraDAO;

	@Inject
	private AnalistaService analistaService;

	@Inject
	private EstudianteService estudianteService;

	private ValidationError validationError = new ValidationError();

	private boolean booleanEsEstudiante;

	private boolean labelVisModPerfil;
	private boolean inputsVisModPerfil;
	private boolean inputsVisMod;
	private boolean menuButtonsRender;

	private List<Usuario> listaUsuarios;
	private String nombreprueba;

	private long idLogueada;

	private Usuario usuarioLogeado;
	private String tipoUsuarioSeleccionado;
	private String estadoUsuarioSeleccionado;
	private String verificacionSeleccionado;
	private Usuario usuarioSelecLista;

	private UsuarioDTO usuarioSeleccionado;
	private EstudianteDTO estudianteSeleccionado;

	private long idSeleccionada;
	private String nombreUsuarioLogin;
	private String contraseniaLogin;

	private String nombreItrSeleccionado;
	private String nombreCarreraSelecc;

	private List<String> listaItrs;
	private List<String> listaCarreras;

	private List<Itr> itrList;
	private Itr itrSeleccionado;

	private boolean renderInputText;

	@PostConstruct
	public void init() {
		labelVisModPerfil = true;
		inputsVisModPerfil = false;
		inputsVisMod = false;
		menuButtonsRender = false;
		renderInputText = false;

		listaUsuarios = new LinkedList<>();
		nombreprueba = null;
		tipoUsuarioSeleccionado = null;
		usuarioSelecLista = new Usuario();

		usuarioSeleccionado = new UsuarioDTO();
		estudianteSeleccionado = new EstudianteDTO();

		idSeleccionada = 0;
		nombreUsuarioLogin = null;
		contraseniaLogin = null;

		nombreItrSeleccionado = null;
		nombreCarreraSelecc = null;

		listaItrs = new ArrayList<>();
		listaCarreras = new ArrayList<>();
	}

	public void rellenarListaCarreras() {
		listaCarreras = new ArrayList<>();
		listaCarreras.add("");

		for (CarreraEspecialidad car : carreraDAO.obtenerTodos()) {
			if(car.getEstadoCarreraEspecialidad().equals(EstadoCarreraEspecialidad.ACTIVADO)) {
				listaCarreras.add(car.getNombre());
			}
			
		}
	}

	public void seleccionarUsuario() {
		listaItrs = new ArrayList<>();
		listaCarreras = new ArrayList<>();
		listaItrs.add("");
		listaCarreras.add("");

		for (Itr itr : itrDAO.obtenerTodos()) {
			if(itr.getEstadoItr().equals(EstadoItr.ACTIVADO)) {
				listaItrs.add(itr.getNombre());
			}	
		}
		for (CarreraEspecialidad car : carreraDAO.obtenerTodos()) {
			if(car.getEstadoCarreraEspecialidad().equals(EstadoCarreraEspecialidad.ACTIVADO)) {
				listaCarreras.add(car.getNombre());
			}
		}

		idSeleccionada = usuarioSelecLista.getIdUsuario();
		usuarioSeleccionado.setNombre(usuarioSelecLista.getNombre());
		usuarioSeleccionado.setApellido(usuarioSelecLista.getApellido());
		usuarioSeleccionado.setDocumento(usuarioSelecLista.getDocumento() + "");
		usuarioSeleccionado.setFechaNacimiento(usuarioSelecLista.getFechaNacimiento());
		usuarioSeleccionado.setMail(usuarioSelecLista.getMail());
		usuarioSeleccionado.setTelefono(usuarioSelecLista.getTelefono());
		tipoUsuarioSeleccionado = usuarioSelecLista.getTipoUsuario().getNombre();
		estadoUsuarioSeleccionado = usuarioSelecLista.getEstadoUsuario().getNombre();
		verificacionSeleccionado = usuarioSelecLista.getVerificacion().getNombre();
		usuarioSeleccionado.setDireccion(usuarioSelecLista.getDireccion());
		nombreItrSeleccionado = usuarioSelecLista.getItr().getNombre();
		usuarioSeleccionado.setNombreUsuario(usuarioSelecLista.getNombreUsuario());
		usuarioSeleccionado.setContrasenia(usuarioSelecLista.getContrasenia());

		if (TipoUsuario.ESTUDIANTE.equals(usuarioSelecLista.getTipoUsuario())) {
			Estudiante est = estudianteDAO.buscarEstudiante(usuarioSelecLista.getIdUsuario());
			nombreCarreraSelecc = est.getCarreraEspecialidad().getNombre();
			estudianteSeleccionado.setIdEstudiantil(est.getIdEstudiantil() + "");
			inputsVisMod = true;
		} else {
			inputsVisMod = false;

		}
	}

	public void limpiarCampos() {
		idSeleccionada = 0;
		usuarioSeleccionado = new UsuarioDTO();
		tipoUsuarioSeleccionado = new String();
		estadoUsuarioSeleccionado = new String();
		verificacionSeleccionado = new String();
		nombreItrSeleccionado = new String();
		nombreCarreraSelecc = new String();
		estudianteSeleccionado = new EstudianteDTO();
	}

	public void showMessage(FacesMessage message) {
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	public void infoMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String cargarMenuRegistro() {
		listaItrs.add("");
		for (Itr itr : itrDAO.obtenerTodos()) {
			if (EstadoItr.ACTIVADO.equals(itr.getEstadoItr())) {
				listaItrs.add(itr.getNombre());
			}

		}

		return "";
	}

	public String cargarRegistro() {
		init();
		cargarListarUsuarios();
		itrList = itrDAO.obtenerTodos();
		
		System.out.println(itrList);
		listaCarreras.add("");
		for (CarreraEspecialidad car : carreraDAO.obtenerTodos()) {
			if(car.getEstadoCarreraEspecialidad().equals(EstadoCarreraEspecialidad.ACTIVADO)) {
				listaCarreras.add(car.getNombre());
			}
		}
		return "registro.xhtml?faces-redirect=true";
	}

	public String cargarListarUsuarios() {
		listaUsuarios = usuarioDAO.obtenerTodos();
		return "/analista/mainContent_Lista_De_Usuarios.xhtml?faces-redirect=true";
	}

	public String cargarMenuPerfil() {
		
		usuarioLogeado = usuarioDAO.buscarUsuario(idLogueada);

		if (TipoUsuario.ESTUDIANTE.equals(usuarioLogeado.getTipoUsuario())) {
			Estudiante est = estudianteDAO.buscarEstudiante(idLogueada);
			menuButtonsRender = false;
			nombreCarreraSelecc = est.getCarreraEspecialidad().getNombre();
			for (CarreraEspecialidad car : carreraDAO.obtenerTodos()) {
				listaCarreras.add(car.getNombre());
			}

		} else {
			menuButtonsRender = true;
		}
		return "/general/mainContent_Perfil_Del_Usuario.xhtml?faces-redirect=true";
	}

	public void registrarUsuario() {
		if (nombreItrSeleccionado != null) {
			itrSeleccionado = itrDAO.obtenerPorNombre(nombreItrSeleccionado);
		}
		usuarioSeleccionado.setItr(itrSeleccionado);
		usuarioSeleccionado.setEstadoUsuario(EstadoUsuario.ACTIVO);
		usuarioSeleccionado.setVerificacion(Verificacion.NO_VERIFICADO);
		
		if (TipoUsuario.ANALISTA.getNombre().equals(tipoUsuarioSeleccionado)) {
			usuarioSeleccionado.setTipoUsuario(TipoUsuario.ANALISTA);
		} else if (TipoUsuario.ESTUDIANTE.getNombre().equals(tipoUsuarioSeleccionado)) {
			CarreraEspecialidad carreraSelecc = null;
			if(nombreCarreraSelecc != null) {
				carreraSelecc = carreraDAO.obtenerPorNombre(nombreCarreraSelecc);
			}
			estudianteSeleccionado.setCarreraEspecialidad(carreraSelecc);
			usuarioSeleccionado.setTipoUsuario(TipoUsuario.ESTUDIANTE);
		}
		
		if (validationError.validarErroresRegistro(usuarioSeleccionado, estudianteSeleccionado)) {
			if (TipoUsuario.ANALISTA.getNombre().equals(tipoUsuarioSeleccionado)) {
				FacesContext context = FacesContext.getCurrentInstance();
				analistaService.registrarAnalista(usuarioSeleccionado);

				usuarioSeleccionado = new UsuarioDTO();
				estudianteSeleccionado = new EstudianteDTO();

				cargarRegistro();
				context.getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarUsuario').hide()");

			} else if (TipoUsuario.ESTUDIANTE.getNombre().equals(tipoUsuarioSeleccionado)) {
				FacesContext context = FacesContext.getCurrentInstance();
				estudianteService.registrarEstudiante(usuarioSeleccionado, estudianteSeleccionado);

				usuarioSeleccionado = new UsuarioDTO();
				estudianteSeleccionado = new EstudianteDTO();

				cargarRegistro();
				context.getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarUsuario').hide()");
			}
		}

	}
	public String modificarDatosPropios() {
		
		idSeleccionada = usuarioLogeado.getIdUsuario();
		if (nombreItrSeleccionado != null) {
			itrSeleccionado = itrDAO.obtenerPorNombre(nombreItrSeleccionado);
		}
		
		usuarioSeleccionado.setItr(itrSeleccionado);
		usuarioSeleccionado.setIdUsuario(usuarioLogeado.getIdUsuario());
		usuarioSeleccionado.setTipoUsuario(usuarioLogeado.getTipoUsuario());
		usuarioSeleccionado.setEstadoUsuario(usuarioLogeado.getEstadoUsuario());
		usuarioSeleccionado.setVerificacion(usuarioLogeado.getVerificacion());
		if (TipoUsuario.ANALISTA.equals(usuarioLogeado.getTipoUsuario())) {
			if (validationError.validarErroresRegistro(usuarioSeleccionado, estudianteSeleccionado)) {
				analistaService.modificarAnalista(usuarioSeleccionado);
				visModPerfil();
				return cargarMenuPerfil();
			}

		} else if (TipoUsuario.ESTUDIANTE.equals(usuarioLogeado.getTipoUsuario())) {
			Estudiante estudiante = estudianteDAO.buscarEstudiante(usuarioLogeado.getIdUsuario());
			CarreraEspecialidad car = carreraDAO.obtenerPorNombre(nombreCarreraSelecc);
			estudianteSeleccionado.setCarreraEspecialidad(car);
			estudianteSeleccionado.setIdEstudiantil(String.valueOf(estudiante.getIdEstudiantil()));

			if (validationError.validarErroresRegistro(usuarioSeleccionado, estudianteSeleccionado)) {
				estudianteService.modificarEstudiante(usuarioSeleccionado, estudianteSeleccionado);
				visModPerfil();
				return cargarMenuPerfil();
			}
		}
		return cargarMenuPerfil();
	}

	public void modificarUsuario() {
		
		usuarioSeleccionado.setIdUsuario(idSeleccionada);
		if(nombreItrSeleccionado != null) {
			itrSeleccionado = itrDAO.obtenerPorNombre(nombreItrSeleccionado);
			usuarioSeleccionado.setItr(itrSeleccionado);
		}
		

		if (Verificacion.VERIFICADO.getNombre().equals(verificacionSeleccionado)) {
			usuarioSeleccionado.setVerificacion(Verificacion.VERIFICADO);
		} else if (Verificacion.NO_VERIFICADO.getNombre().equals(verificacionSeleccionado)) {
			usuarioSeleccionado.setVerificacion(Verificacion.NO_VERIFICADO);
		} else {
			usuarioSeleccionado.setVerificacion(null);
		}

		if (EstadoUsuario.ACTIVO.getNombre().equals(estadoUsuarioSeleccionado)) {
			usuarioSeleccionado.setEstadoUsuario(EstadoUsuario.ACTIVO);
		} else if (EstadoUsuario.INACTIVO.getNombre().equals(estadoUsuarioSeleccionado)) {
			usuarioSeleccionado.setEstadoUsuario(EstadoUsuario.INACTIVO);
		} else {
			usuarioSeleccionado.setEstadoUsuario(null);
		}
		
		if (TipoUsuario.ANALISTA.getNombre().equals(tipoUsuarioSeleccionado)) {
			usuarioSeleccionado.setTipoUsuario(TipoUsuario.ANALISTA);

		} else if (TipoUsuario.ESTUDIANTE.getNombre().equals(tipoUsuarioSeleccionado)) {
			if(nombreCarreraSelecc != null) {
				estudianteSeleccionado.setCarreraEspecialidad(carreraDAO.obtenerPorNombre(nombreCarreraSelecc));
			}
			usuarioSeleccionado.setTipoUsuario(TipoUsuario.ESTUDIANTE);
		}
		if (TipoUsuario.ANALISTA.getNombre().equals(tipoUsuarioSeleccionado)) {
			
			if (validationError.validarErroresRegistro(usuarioSeleccionado, estudianteSeleccionado)) {
				FacesContext context = FacesContext.getCurrentInstance();
				
				analistaService.modificarAnalista(usuarioSeleccionado);
				limpiarCampos();
				cargarListarUsuarios();
				context.getPartialViewContext().getEvalScripts().add("PF('dialogModificarUsuario').hide()");
			}

		} else if (TipoUsuario.ESTUDIANTE.getNombre().equals(tipoUsuarioSeleccionado)) {

			if (validationError.validarErroresRegistro(usuarioSeleccionado, estudianteSeleccionado)) {
				FacesContext context = FacesContext.getCurrentInstance();
				estudianteService.modificarEstudiante(usuarioSeleccionado, estudianteSeleccionado);
				limpiarCampos();
				cargarListarUsuarios();
				context.getPartialViewContext().getEvalScripts().add("PF('dialogModificarUsuario').hide()");
			}
		}else {
			validationError.validarErroresRegistro(usuarioSeleccionado, estudianteSeleccionado);
		}

	}

	public void borrarUsuario(long id) {
		usuarioDAO.borrarLogicamente(id);
		cargarListarUsuarios();
	}

	public String visModPerfil() {
		listaItrs = new ArrayList<>();
		listaCarreras = new ArrayList<>();
		listaItrs.add("");
		listaCarreras.add("");
		for (Itr itr : itrDAO.obtenerTodos()) {
			if(itr.getEstadoItr().equals(EstadoItr.ACTIVADO)) {
				listaItrs.add(itr.getNombre());
			}	
		}
		for (CarreraEspecialidad car : carreraDAO.obtenerTodos()) {
			if(car.getEstadoCarreraEspecialidad().equals(EstadoCarreraEspecialidad.ACTIVADO)) {
				listaCarreras.add(car.getNombre());
			}
		}

		Usuario usu = usuarioDAO.buscarUsuario(idLogueada);

		usuarioSeleccionado.setNombre(usu.getNombre());
		usuarioSeleccionado.setApellido(usu.getApellido());
		usuarioSeleccionado.setDocumento(String.valueOf(usu.getDocumento()));
		usuarioSeleccionado.setFechaNacimiento(usu.getFechaNacimiento());
		usuarioSeleccionado.setDireccion(usu.getDireccion());
		usuarioSeleccionado.setNombreUsuario(usu.getNombreUsuario());
		usuarioSeleccionado.setContrasenia(usu.getContrasenia());
		usuarioSeleccionado.setTipoUsuario(usu.getTipoUsuario());
		usuarioSeleccionado.setVerificacion(usu.getVerificacion());
		usuarioSeleccionado.setMail(usu.getMail());
		usuarioSeleccionado.setTelefono(usu.getTelefono());
		usuarioSeleccionado.setItr(usu.getItr());
		nombreItrSeleccionado = usu.getItr().getNombre();

		if (labelVisModPerfil == true) {
			labelVisModPerfil = false;
			inputsVisModPerfil = true;
		} else {
			labelVisModPerfil = true;
			inputsVisModPerfil = false;
		}

		return "";
	}

	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isBlank(filterText)) {
			return true;
		}

		Usuario usuario = (Usuario) value;
		return (usuario.getIdUsuario() + "").toLowerCase().contains(filterText)
				|| usuario.getNombre().toLowerCase().contains(filterText)
				|| usuario.getApellido().toLowerCase().contains(filterText)
				|| (usuario.getDocumento() + "").toLowerCase().contains(filterText)
				|| usuario.getItr().getNombre().toLowerCase().contains(filterText)
				|| usuario.getTipoUsuario().getNombre().toLowerCase().contains(filterText)
				|| usuario.getVerificacion().getNombre().toLowerCase().contains(filterText)
				|| usuario.getEstadoUsuario().getNombre().toLowerCase().contains(filterText);
	}

	public void menuItemChanged() {
		if ("Estudiante".equals(tipoUsuarioSeleccionado)) {
			renderInputText = true;
		} else {
			renderInputText = false;
		}
	}

	public List<String> cargarListaItr() {
		listaItrs = new ArrayList<>();
		listaItrs.add("");
		for (Itr itr : itrDAO.obtenerTodos()) {
			if (EstadoItr.ACTIVADO.equals(itr.getEstadoItr())) {
				listaItrs.add(itr.getNombre());
			}

		}
		return listaItrs;
	}

	public boolean renderModPerfilEstudiante() {
		if (inputsVisModPerfil && !menuButtonsRender) {
			return true;
		} else {
			return false;
		}

	}

	public boolean renderModPerfilEstudiante2() {
		if (labelVisModPerfil && !menuButtonsRender) {
			return true;
		} else {
			return false;
		}

	}

	public boolean renderLabelModPerfilEst() {
		if (!menuButtonsRender) {
			return true;
		} else {
			return false;
		}
	}

}

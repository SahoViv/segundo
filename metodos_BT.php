<?php
class MetodosBT {

    private $conexion;

    // 🔹 CONEXIÓN A LA BASE DE DATOS
    public function conectarBD() {
        try {
            $this->conexion = new PDO(
                "mysql:host=localhost;dbname=bolsa_trabajo;charset=utf8",
                "root",
                ""
            );
            $this->conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            // echo "Conectado correctamente";
        } catch (PDOException $e) {
            die("Error de conexión: " . $e->getMessage());
        }
    }

    // 🔹 AGREGAR POSTULANTE
    public function agregarPostulante($nombre, $a_pat, $a_mat, $correo_p, $matricula, $carrera, $opcion_modelo) {
        $this->conectarBD();

        $sql = "INSERT INTO postulante 
                (nombre, a_pat, a_mat, correo_p, matricula, carrera, opcion_de_modelo)
                VALUES (?, ?, ?, ?, ?, ?, ?)";

        $stmt = $this->conexion->prepare($sql);
        return $stmt->execute([$nombre, $a_pat, $a_mat, $correo_p, $matricula, $carrera, $opcion_modelo]);
    }

    // 🔹 AGREGAR VACANTE
    public function agregarVacante($nombre_empresa, $puesto_vacante, $ubi_v, $descripcion_v, $sueldo) {
        $this->conectarBD();

        $sql = "INSERT INTO vacante 
                (nombre_empresa, puesto_vacante, ubi_v, descripcion_v, sueldo)
                VALUES (?, ?, ?, ?, ?)";

        $stmt = $this->conexion->prepare($sql);
        return $stmt->execute([$nombre_empresa, $puesto_vacante, $ubi_v, $descripcion_v, $sueldo]);
    }

    // 🔹 OBTENER VACANTES (para llenar un select en HTML)
    public function obtenerVacantes() {
        $this->conectarBD();

        $sql = "SELECT puesto_vacante FROM vacante";
        $stmt = $this->conexion->query($sql);

        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    // 🔹 MOSTRAR DETALLES DE VACANTE
    public function mostrarDetallesVacante($vacanteSel) {
        $this->conectarBD();

        $sql = "SELECT id_vacante, nombre_empresa, puesto_vacante, 
                       ubi_v, sueldo, descripcion_v
                FROM vacante 
                WHERE TRIM(puesto_vacante) = ?";

        $stmt = $this->conexion->prepare($sql);
        $stmt->execute([$vacanteSel]);

        return $stmt->fetch(PDO::FETCH_ASSOC);
    }

    // 🔹 MODIFICAR VACANTE
    public function modificarVacante($nombre_empresa, $puesto_vacante, $ubi_v, $descripcion_v, $sueldo, $id_vacante) {
        $this->conectarBD();

        $sql = "UPDATE vacante 
                SET nombre_empresa=?, puesto_vacante=?, ubi_v=?, descripcion_v=?, sueldo=? 
                WHERE id_vacante=?";

        $stmt = $this->conexion->prepare($sql);
        return $stmt->execute([$nombre_empresa, $puesto_vacante, $ubi_v, $descripcion_v, $sueldo, $id_vacante]);
    }

    // 🔹 ELIMINAR VACANTE
    public function eliminarVacante($id_vacante) {
        $this->conectarBD();

        $sql = "DELETE FROM vacante WHERE id_vacante=?";
        $stmt = $this->conexion->prepare($sql);

        return $stmt->execute([$id_vacante]);
    }

    // 🔹 BUSCAR POSTULANTE (LOGIN)
    public function buscarPostulante($correo_p) {
        $this->conectarBD();

        $sql = "SELECT id_postulante, nombre, a_pat, a_mat 
                FROM postulante 
                WHERE correo_p=?";

        $stmt = $this->conexion->prepare($sql);
        $stmt->execute([$correo_p]);

        return $stmt->fetch(PDO::FETCH_ASSOC);
    }

    // 🔹 VALIDAR LOGIN
    public function validarLogin($correo_p, $matricula) {
        $this->conectarBD();

        $sql = "SELECT nombre 
                FROM postulante 
                WHERE correo_p=? AND matricula=?";

        $stmt = $this->conexion->prepare($sql);
        $stmt->execute([$correo_p, $matricula]);

        return $stmt->rowCount() > 0;
    }

    // 🔹 SUBIR CV (PDF)
    public function subirCV($id_vacante, $id_postulante, $archivoTmp) {
        $this->conectarBD();

        $sql = "INSERT INTO tabla_v_p (id_vacante, id_postulante, cv)
                VALUES (?, ?, ?)";

        $stmt = $this->conexion->prepare($sql);

        $contenido = file_get_contents($archivoTmp);

        return $stmt->execute([$id_vacante, $id_postulante, $contenido]);
    }

    // 🔹 MOSTRAR TABLA CON JOIN
    public function mostrarTabla() {
        $this->conectarBD();

        $sql = "SELECT puesto_vacante, correo_p, matricula, carrera, opcion_de_modelo
                FROM vacante
                INNER JOIN tabla_v_p ON vacante.id_vacante = tabla_v_p.id_vacante
                INNER JOIN postulante ON tabla_v_p.id_postulante = postulante.id_postulante";

        $stmt = $this->conexion->query($sql);

        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }
}
?>
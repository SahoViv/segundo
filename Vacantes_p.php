<?php
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
session_start();
require_once "MetodosBT.php";

/**
 * Alumnos: BERNAL MARTINEZ RICARDO y OLGUIN VIVIAN YARED SAHORI
 * Grupo: 3DSM5
 */

$metodo = new MetodosBT();

// Se llama al método para rellenar las vacantes (equivalente a rellenarvacantes(jComboBox1))
$vacantes = $metodo->obtenerVacantes();

$detalle = null;
$mensaje = "";

/* =========================
   jButton4ActionPerformed
   VER VACANTE
========================= */
if (isset($_POST['ver_vacante'])) {

    // Obtiene la vacante seleccionada
    $seleccion = $_POST['vacante'];

    if (empty($seleccion)) {
        $mensaje = "Por favor seleccione una vacante";
    } else {
        $detalle = $metodo->mostrarDetallesVacante($seleccion);
    }
}

/* =========================
   jButton2ActionPerformed
   AGREGAR CV (PDF)
========================= */
if (isset($_POST['subir_cv'])) {

    if (!empty($_FILES['cv']['tmp_name'])) {

        // Obtiene los identificadores igual que jLabel13 y jTextField6
        $id_postulante = $_SESSION['id_postulante'];
        $id_vacante = $_POST['id_vacante'];

        // Obtiene el archivo PDF
        $cv = $_FILES['cv']['tmp_name'];

        // Llama al método tabla(id_vacante, id_postulante, cv)
        $metodo->subirCV($id_vacante, $id_postulante, $cv);

        $mensaje = "Se ha subido correctamente tu CV";
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>VACANTES</title>
</head>
<body>

<h2>VACANTES</h2>

<!-- Mensajes tipo JOptionPane -->
<?php if ($mensaje != ""): ?>
    <p><strong><?= $mensaje ?></strong></p>
<?php endif; ?>

<!-- =========================
     jComboBox1
     Vacantes Disponibles
========================= -->
<form method="POST">

    <label>Vacantes Disponibles:</label><br>
    <select name="vacante">
        <option value="">Seleccione</option>
        <?php foreach ($vacantes as $v): ?>
            <option value="<?= $v['puesto_vacante'] ?>">
                <?= $v['puesto_vacante'] ?>
            </option>
        <?php endforeach; ?>
    </select>

    <br><br>

    <!-- jButton4 -->
    <button type="submit" name="ver_vacante">
        Ver Vacante
    </button>

    <!-- jButton5ActionPerformed
         LIMPIAR CAMPOS -->
    <button type="reset">
        Limpiar Campos
    </button>

</form>

<hr>

<!-- =========================
     MOSTRAR DETALLES DE VACANTE
     Equivalente a mostrarDetallesVacante(...)
========================= -->
<?php if ($detalle): ?>

<form method="POST" enctype="multipart/form-data">

    <!-- Identificador de la Vacante -->
    <input type="hidden" name="id_vacante" value="<?= $detalle['id_vacante'] ?>">

    <label>Nombre de Empresa:</label><br>
    <input type="text" value="<?= $detalle['nombre_empresa'] ?>" readonly><br><br>

    <label>Puesto de Vacante:</label><br>
    <input type="text" value="<?= $detalle['puesto_vacante'] ?>" readonly><br><br>

    <label>Ubicación:</label><br>
    <input type="text" value="<?= $detalle['ubi_v'] ?>" readonly><br><br>

    <label>Sueldo:</label><br>
    <input type="text" value="<?= $detalle['sueldo'] ?>" readonly><br><br>

    <label>Descripción:</label><br>
    <textarea readonly><?= $detalle['descripcion_v'] ?></textarea><br><br>

    <!-- jButton2
         Agregar CV (PDF) -->
    <label>Agregar CV (PDF):</label><br>
    <input type="file" name="cv" accept="application/pdf" required><br><br>

    <button type="submit" name="subir_cv">
        Subir CV
    </button>

</form>

<?php endif; ?>

<hr>

<!-- =========================
     jButton3ActionPerformed
     Abre el manual PDF
========================= -->
<a href="img/MANUAL PARA POSTULANTE.pdf" target="_blank">
    Ver Manual para Postulante
</a>

<br><br>

<!-- =========================
     jButton1ActionPerformed
     Regresar
========================= -->
<a href="Inicio_s_p.php">
    Regresar
</a>

</body>
</html>
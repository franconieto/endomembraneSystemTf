import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import os
from glob import glob
import math

# Configuración
base_dir = os.path.join(os.path.dirname(__file__), 'grupos')
output_dir = os.path.join(os.path.dirname(base_dir), "output")
os.makedirs(output_dir, exist_ok=True)
grupos = ['tropism_1', 'tropism_0.1']
rab_columns = ['RabA', 'RabB', 'RabC', 'RabD']

def get_sim_data(grupo_path, rab_columns):
    """Lee todos los csv de un grupo y devuelve un dict con los datos relevantes por simulación."""
    csv_files = glob(os.path.join(grupo_path, '*.csv'))
    sim_data = []
    for csv_file in csv_files:
        df = pd.read_csv(csv_file)
        sim_name = os.path.splitext(os.path.basename(csv_file))[0]
        data = {'simulacion': sim_name, 'df': df}
        # Melt para Rabs
        existing_rab_columns = [col for col in rab_columns if col in df.columns]
        if existing_rab_columns:
            data['df_rabs'] = df.melt(id_vars='tick', value_vars=existing_rab_columns,
                                      var_name='Rab', value_name='Valor')
        else:
            data['df_rabs'] = None
        sim_data.append(data)
    return sim_data

def plot_rab_distribuciones(sim_data, grupo_path):
    """Genera y guarda los gráficos individuales de distribución de Rabs."""
    for data in sim_data:
        df_melted = data['df_rabs']
        if df_melted is not None:
            plt.figure(figsize=(12, 6))
            sns.lineplot(data=df_melted, x='tick', y='Valor', hue='Rab', palette='tab10')
            plt.title(f"Distribución de Rabs en fagosoma\n{data['simulacion']}")
            plt.xlabel("Tick")
            plt.ylabel("Valor asociado al Rab")
            plt.legend(title="Rab")
            plt.tight_layout()
            out_path = os.path.join(grupo_path, f"{data['simulacion']}_plot.png")
            plt.savefig(out_path)
            plt.close()

def plot_rab_promedio(sim_data, grupo, grupo_path):
    """Genera y guarda el gráfico de promedios de Rabs por grupo."""
    dfs = [d['df_rabs'] for d in sim_data if d['df_rabs'] is not None]
    if dfs:
        df_concat = pd.concat(dfs)
        df_mean = df_concat.groupby(['tick', 'Rab'])['Valor'].mean().reset_index()
        plt.figure(figsize=(12, 6))
        sns.lineplot(data=df_mean, x='tick', y='Valor', hue='Rab', palette='tab10')
        plt.title(f"Promedio de distribución de Rabs en fagosoma\nGrupo: {grupo}")
        plt.xlabel("Tick")
        plt.ylabel("Valor promedio asociado al Rab")
        plt.legend(title="Rab")
        plt.tight_layout()
        out_mean_path = os.path.join(grupo_path, f"{grupo}_mean_plot.png")
        plt.savefig(out_mean_path)
        plt.close()
    return dfs

def plot_metric_simulaciones(sim_data, grupo_path, col, ylabel, titulo, outname):
    """Genera y guarda gráficos de una métrica (area, volume, RabD, etc) por simulación y promedio."""
    dfs = []
    for data in sim_data:
        df = data['df']
        if col in df.columns:
            df_aux = df[['tick', col]].copy()
            df_aux['simulacion'] = data['simulacion']
            dfs.append(df_aux)
    if dfs:
        all_df = pd.concat(dfs)
        mean_df = all_df.groupby('tick')[col].mean().reset_index()
        plt.figure(figsize=(12, 6))
        for sim, sim_df in all_df.groupby('simulacion'):
            plt.plot(sim_df['tick'], sim_df[col], alpha=0.7, linewidth=1, label=sim)
        plt.plot(mean_df['tick'], mean_df[col], color='black', linewidth=2.5, label='Promedio')
        plt.title(f"{titulo}")
        plt.xlabel("Tick")
        plt.ylabel(ylabel)
        plt.legend(fontsize='small', ncol=2)
        plt.tight_layout()
        plt.savefig(os.path.join(grupo_path, outname))
        plt.close()
    return dfs

def plot_rabd_pct_area(sim_data, grupo_path):
    """Genera y guarda el gráfico de %RabD respecto a area por simulación y promedio."""
    dfs = []
    for data in sim_data:
        df = data['df']
        if 'RabD' in df.columns and 'area' in df.columns:
            perc_df = df[['tick', 'RabD', 'area']].copy()
            perc_df['RabD_area_pct'] = (perc_df['RabD'] / perc_df['area']) * 100
            perc_df['simulacion'] = data['simulacion']
            dfs.append(perc_df)
    if dfs:
        perc_all = pd.concat(dfs)
        perc_mean = perc_all.groupby('tick')['RabD_area_pct'].mean().reset_index()
        plt.figure(figsize=(12, 6))
        for sim, sim_df in perc_all.groupby('simulacion'):
            plt.plot(sim_df['tick'], sim_df['RabD_area_pct'], alpha=0.7, linewidth=1, label=sim)
        plt.plot(perc_mean['tick'], perc_mean['RabD_area_pct'], color='black', linewidth=2.5, label='Promedio')
        plt.title("% RabD respecto a área por simulación y promedio")
        plt.xlabel("Tick")
        plt.ylabel("% RabD / área")
        plt.legend(fontsize='small', ncol=2)
        plt.tight_layout()
        plt.savefig(os.path.join(grupo_path, "RabD_porcentaje_area.png"))
        plt.close()
    return dfs

def get_metric_mean(sim_data, col, grupo):
    """Devuelve un DataFrame con el promedio por tick de una métrica para un grupo."""
    dfs = []
    for data in sim_data:
        df = data['df']
        if col in df.columns:
            df_aux = df[['tick', col]].copy()
            dfs.append(df_aux)
    if dfs:
        all_df = pd.concat(dfs)
        mean_df = all_df.groupby('tick')[col].mean().reset_index()
        mean_df['Grupo'] = grupo
        return mean_df
    return None

def get_rabd_pct_area_mean(sim_data, grupo):
    """Devuelve un DataFrame con el promedio por tick de %RabD/area para un grupo."""
    dfs = []
    for data in sim_data:
        df = data['df']
        if 'RabD' in df.columns and 'area' in df.columns:
            perc_df = df[['tick', 'RabD', 'area']].copy()
            perc_df['RabD_area_pct'] = (perc_df['RabD'] / perc_df['area']) * 100
            dfs.append(perc_df)
    if dfs:
        perc_all = pd.concat(dfs)
        perc_mean = perc_all.groupby('tick')['RabD_area_pct'].mean().reset_index()
        perc_mean['Grupo'] = grupo
        return perc_mean
    return None

def plot_comparacion_metricas(means, col, ylabel, titulo, outname):
    """Grafica la comparación entre grupos para una métrica."""
    if means:
        plt.figure(figsize=(12, 6))
        for df in means:
            plt.plot(df['tick'], df[col], label=df['Grupo'].iloc[0])
        plt.title(titulo)
        plt.xlabel("Tick")
        plt.ylabel(ylabel)
        plt.legend()
        plt.tight_layout()
        plt.savefig(outname)
        plt.close()

def plot_comparacion_rabd_pct_area(means, ylabel, titulo, outname):
    """Grafica la comparación entre grupos para %RabD/area."""
    if means:
        plt.figure(figsize=(12, 6))
        for df in means:
            plt.plot(df['tick'], df['RabD_area_pct'], label=df['Grupo'].iloc[0])
        plt.title(titulo)
        plt.xlabel("Tick")
        plt.ylabel(ylabel)
        plt.legend()
        plt.tight_layout()
        plt.savefig(outname)
        plt.close()

def agrupar_imagenes(imagenes, titulos, fig_title, out_path):
    """Agrupa hasta 4 imágenes en un solo PNG con títulos individuales."""
    n = len(imagenes)
    fig, axs = plt.subplots(2, 2, figsize=(18, 12))
    fig.suptitle(fig_title, fontsize=18)
    for i in range(4):
        ax = axs[i // 2, i % 2]
        if i < n and os.path.exists(imagenes[i]):
            img = plt.imread(imagenes[i])
            ax.imshow(img)
            ax.set_title(titulos[i])
            ax.axis('off')
        else:
            ax.axis('off')
    plt.tight_layout(rect=[0, 0, 1, 0.96])
    plt.savefig(out_path)
    plt.close()

def agrupar_rab_distribuciones(grupo, grupo_path, output_dir):
    """Agrupa los gráficos de distribución de Rabs de cada simulación en páginas de 4."""
    rab_files = sorted([f for f in os.listdir(grupo_path) if f.endswith('_plot.png')])
    n = len(rab_files)
    n_pages = math.ceil(n / 4)
    for page in range(n_pages):
        imgs = [os.path.join(grupo_path, rab_files[page*4 + i]) for i in range(4) if page*4 + i < n]
        titulos = [rab_files[page*4 + i].replace('_plot.png', '') for i in range(4) if page*4 + i < n]
        out_img = os.path.join(output_dir, f"{grupo}_distribucion_rabs_pagina_{page+1}.png")
        agrupar_imagenes(imgs, titulos, f"Distribución de Rabs en fagosoma - {grupo} (página {page+1})", out_img)

def agrupar_intra_grupo(grupo, grupo_path, output_dir):
    """Agrupa los gráficos de área, volumen, RabD y %RabD/area por simulación y promedio."""
    imgs = [
        os.path.join(grupo_path, "Area_por_simulacion.png"),
        os.path.join(grupo_path, "Volume_por_simulacion.png"),
        os.path.join(grupo_path, "RabD_por_simulacion.png"),
        os.path.join(grupo_path, "RabD_porcentaje_area.png"),
    ]
    titulos = [
        "Área por simulación",
        "Volumen por simulación",
        "RabD por simulación",
        "% RabD respecto a área por simulación"
    ]
    out_img = os.path.join(output_dir, f"{grupo}_comparaciones_intra_grupo.png")
    agrupar_imagenes(imgs, titulos, f"Comparaciones intra-grupo - {grupo}", out_img)

def agrupar_entre_grupos(base_dir, output_dir):
    """Agrupa los gráficos comparativos entre grupos."""
    imgs = [
        os.path.join(base_dir, "comparacion_area.png"),
        os.path.join(base_dir, "comparacion_volume.png"),
        os.path.join(base_dir, "comparacion_rabD.png"),
        os.path.join(base_dir, "comparacion_rabD_pct_area.png"),
    ]
    titulos = [
        "Área promedio entre grupos",
        "Volumen promedio entre grupos",
        "RabD promedio entre grupos",
        "% RabD respecto a área promedio entre grupos"
    ]
    out_img = os.path.join(output_dir, "comparaciones_entre_grupos.png")
    agrupar_imagenes(imgs, titulos, "Comparaciones entre grupos", out_img)

# --- PROCESAMIENTO PRINCIPAL ---

# 1. Procesar cada grupo y guardar gráficos individuales y promedios
all_sim_data = {}
for grupo in grupos:
    grupo_path = os.path.join(base_dir, grupo)
    sim_data = get_sim_data(grupo_path, rab_columns)
    all_sim_data[grupo] = sim_data
    plot_rab_distribuciones(sim_data, grupo_path)
    plot_rab_promedio(sim_data, grupo, grupo_path)
    plot_metric_simulaciones(sim_data, grupo_path, 'RabD', 'RabD', "RabD por simulación y promedio", "RabD_por_simulacion.png")
    plot_metric_simulaciones(sim_data, grupo_path, 'area', 'Área', "Área por simulación y promedio", "Area_por_simulacion.png")
    plot_metric_simulaciones(sim_data, grupo_path, 'volume', 'Volumen', "Volumen por simulación y promedio", "Volume_por_simulacion.png")
    plot_rabd_pct_area(sim_data, grupo_path)

# 2. Gráfico de promedios comparativo entre grupos para Rabs
mean_dfs = []
for grupo in grupos:
    sim_data = all_sim_data[grupo]
    dfs = [d['df_rabs'] for d in sim_data if d['df_rabs'] is not None]
    if dfs:
        df_concat = pd.concat(dfs)
        df_mean = df_concat.groupby(['tick', 'Rab'])['Valor'].mean().reset_index()
        df_mean['Grupo'] = grupo
        mean_dfs.append(df_mean)
if mean_dfs:
    df_all_means = pd.concat(mean_dfs)
    plt.figure(figsize=(14, 7))
    sns.lineplot(
        data=df_all_means,
        x='tick',
        y='Valor',
        hue='Rab',
        style='Grupo',
        palette='tab10',
        dashes=True
    )
    plt.title("Comparación de promedios de distribución de Rabs entre grupos")
    plt.xlabel("Tick")
    plt.ylabel("Valor promedio asociado al Rab")
    plt.legend(title="Rab / Grupo", bbox_to_anchor=(1.05, 1), loc='upper left')
    plt.tight_layout()
    out_path = os.path.join(base_dir, "comparacion_promedios_grupos.png")
    plt.savefig(out_path)
    plt.close()

# 3. Gráficos comparativos de métricas entre grupos
area_means = []
volume_means = []
rabd_means = []
rabd_pct_means = []
for grupo in grupos:
    sim_data = all_sim_data[grupo]
    area_mean = get_metric_mean(sim_data, 'area', grupo)
    if area_mean is not None:
        area_means.append(area_mean)
    volume_mean = get_metric_mean(sim_data, 'volume', grupo)
    if volume_mean is not None:
        volume_means.append(volume_mean)
    rabd_mean = get_metric_mean(sim_data, 'RabD', grupo)
    if rabd_mean is not None:
        rabd_means.append(rabd_mean)
    rabd_pct_mean = get_rabd_pct_area_mean(sim_data, grupo)
    if rabd_pct_mean is not None:
        rabd_pct_means.append(rabd_pct_mean)

plot_comparacion_metricas(area_means, 'area', "Área promedio", "Comparación de área promedio entre grupos", os.path.join(base_dir, "comparacion_area.png"))
plot_comparacion_metricas(volume_means, 'volume', "Volumen promedio", "Comparación de volumen promedio entre grupos", os.path.join(base_dir, "comparacion_volume.png"))
plot_comparacion_metricas(rabd_means, 'RabD', "RabD promedio", "Comparación de RabD promedio entre grupos", os.path.join(base_dir, "comparacion_rabD.png"))
plot_comparacion_rabd_pct_area(rabd_pct_means, "% RabD / área promedio", "Comparación de % RabD respecto a área promedio entre grupos", os.path.join(base_dir, "comparacion_rabD_pct_area.png"))

# 4. Agrupar imágenes finales
for grupo in grupos:
    grupo_path = os.path.join(base_dir, grupo)
    agrupar_rab_distribuciones(grupo, grupo_path, output_dir)
    agrupar_intra_grupo(grupo, grupo_path, output_dir)
agrupar_entre_grupos(base_dir, output_dir)